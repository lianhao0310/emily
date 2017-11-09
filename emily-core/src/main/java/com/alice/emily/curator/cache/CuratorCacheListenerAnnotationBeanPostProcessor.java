package com.alice.emily.curator.cache;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.alice.emily.core.SpringContext;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.converter.GenericMessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.HeaderMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.HeadersMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lianhao on 2017/6/13.
 */
public class CuratorCacheListenerAnnotationBeanPostProcessor
        implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {

    private final CuratorCacheHandlerMethodFactoryAdapter messageHandlerMethodFactory =
            new CuratorCacheHandlerMethodFactoryAdapter();

    private final Set<Class<?>> nonAnnotatedClasses =
            Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));

    private BeanFactory beanFactory;

    private Map<CacheListenerKey, CuratorCacheListenerAdapter> listenerAdapters = Maps.newHashMap();

    @Override
    public void afterSingletonsInstantiated() {
        CuratorCacheListenerContainer cacheListenerManager =
                this.beanFactory.getBean(CuratorCacheListenerContainer.class);
        if (!listenerAdapters.isEmpty()) {
            Set<CuratorCacheListenerAdapter> containerSet = Sets.newHashSet(listenerAdapters.values());
            cacheListenerManager.setContainers(containerSet);
        }
        cacheListenerManager.start();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void setBeanFactory(@Nonnull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(@Nonnull Object bean, String beanName) throws BeansException {
        if (!this.nonAnnotatedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            Map<Method, CuratorCacheListener> methods = MethodIntrospector.selectMethods(targetClass,
                    new MethodIntrospector.MetadataLookup<CuratorCacheListener>() {
                        @Override
                        public CuratorCacheListener inspect(@Nonnull Method method) {
                            return AnnotationUtils.findAnnotation(method, CuratorCacheListener.class);
                        }
                    });
            if (CollectionUtils.isEmpty(methods)) {
                this.nonAnnotatedClasses.add(bean.getClass());
            } else {
                methods.forEach((m, c) -> addToContainers(bean, m, c));
            }
        }
        return bean;
    }

    private void addToContainers(Object bean, Method method, CuratorCacheListener curatorCacheListener) {
        Method methodToUse = checkProxy(method, bean);
        InvocableHandlerMethod handlerMethod = messageHandlerMethodFactory
                .createInvocableHandlerMethod(bean, methodToUse);

        for (String path : curatorCacheListener.value()) {
            CacheListenerKey key = new CacheListenerKey(path, curatorCacheListener.type());
            CuratorCacheListenerAdapter container = listenerAdapters
                    .computeIfAbsent(key, k -> new CuratorCacheListenerAdapter(k.path, k.type));
            container.addHandlerMethod(handlerMethod);
        }
    }

    private Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                // Found a @CuratorCacheListener method on the target class for this JDK proxy ->
                // is it also present on the proxy itself?
                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod(method.getName(), method.getParameterTypes());
                        break;
                    } catch (NoSuchMethodException ignored) {
                    }
                }
            } catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            } catch (NoSuchMethodException ex) {
                throw new IllegalStateException(String.format(
                        "@CuratorCacheListener method '%s' found on bean target class '%s', " +
                                "but not found in any interface(s) for bean JDK proxy. Either " +
                                "pull the method up to an interface or switch to subclass (CGLIB) " +
                                "proxies by setting proxy-target-class/proxyTargetClass " +
                                "attribute to 'true'", method.getName(), method.getDeclaringClass().getSimpleName()), ex);
            }
        }
        return method;
    }

    @Value
    @AllArgsConstructor
    private static class CacheListenerKey {
        private String path;
        private CuratorCacheType type;
    }

    /**
     * An {@link MessageHandlerMethodFactory} adapter that offers a configurable underlying
     * instance to use. Useful if the factory to use is determined once the endpoints
     * have been registered but not created yet.
     */
    private class CuratorCacheHandlerMethodFactoryAdapter implements MessageHandlerMethodFactory {

        private MessageHandlerMethodFactory messageHandlerMethodFactory;

        @Override
        public InvocableHandlerMethod createInvocableHandlerMethod(Object bean, Method method) {
            return getMessageHandlerMethodFactory().createInvocableHandlerMethod(bean, method);
        }

        private MessageHandlerMethodFactory getMessageHandlerMethodFactory() {
            if (this.messageHandlerMethodFactory == null) {
                this.messageHandlerMethodFactory = createDefaultMessageHandlerMethodFactory();
            }
            return this.messageHandlerMethodFactory;
        }

        private MessageHandlerMethodFactory createDefaultMessageHandlerMethodFactory() {
            DefaultMessageHandlerMethodFactory defaultFactory = new DefaultMessageHandlerMethodFactory();
            BeanFactory beanFactory = CuratorCacheListenerAnnotationBeanPostProcessor.this.beanFactory;
            defaultFactory.setBeanFactory(beanFactory);

            ConfigurableBeanFactory cbf = (beanFactory instanceof ConfigurableBeanFactory ?
                    (ConfigurableBeanFactory) beanFactory : null);
            ConversionService conversionService = SpringContext.conversionService();

            defaultFactory.setConversionService(conversionService);

            List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

            // Annotation-based argument resolution
            argumentResolvers.add(new HeaderMethodArgumentResolver(conversionService, cbf));
            argumentResolvers.add(new HeadersMethodArgumentResolver());

            // Type-based argument resolution
            GenericMessageConverter messageConverter = new GenericMessageConverter(conversionService);
            argumentResolvers.add(new MessageMethodArgumentResolver(messageConverter));
            argumentResolvers.add(new PayloadArgumentResolver(messageConverter, SpringContext.springValidator()));
            defaultFactory.setArgumentResolvers(argumentResolvers);

            defaultFactory.afterPropertiesSet();
            return defaultFactory;
        }

    }
}
