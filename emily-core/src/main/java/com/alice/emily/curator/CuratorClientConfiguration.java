package com.alice.emily.curator;

import com.google.common.reflect.Reflection;
import com.alice.emily.core.AbstractBeanPostProcessorsRegistrar;
import com.alice.emily.curator.cache.CuratorCacheListenerAnnotationBeanPostProcessor;
import com.alice.emily.curator.cache.CuratorCacheListenerContainer;
import lombok.extern.log4j.Log4j2;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Base64Utils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Created by lianhao on 2017/6/13.
 */
@Log4j2
@Configuration
@ConditionalOnProperty(prefix = "emily.curator", name = "connect-string")
@ConditionalOnClass({ ZooKeeper.class, CuratorFramework.class })
public class CuratorClientConfiguration {

    private final CuratorProperties properties;

    public CuratorClientConfiguration(CuratorProperties properties) {
        this.properties = properties;
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(CuratorFramework.class)
    public CuratorFramework curatorFramework() {

        CuratorFramework client = createCuratorBuilder().build();

        return Reflection.newProxy(CuratorFramework.class, (proxy, method, args) -> {
            if (!"close".equals(method.getName())
                    && client.getState() == CuratorFrameworkState.LATENT) {
                client.start();
            }
            return method.invoke(client, args);
        });

    }

    @Bean
    @ConditionalOnMissingBean
    public CuratorTemplate curatorTemplate(CuratorFramework client,
                                           ThreadPoolTaskExecutor executor,
                                           ConversionService conversionService) {
        return new CuratorTemplate(client, executor, conversionService);
    }

    @Configuration
    @ConditionalOnClass({ InvocableHandlerMethod.class })
    @Import(CuratorCacheListenerConfiguration.CuratorCacheListenerBeanPostRegistrar.class)
    static class CuratorCacheListenerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public CuratorCacheListenerContainer curatorCacheListenerContainer(CuratorOperations operations,
                                                                           ThreadPoolTaskExecutor executor) {
            return new CuratorCacheListenerContainer(operations, executor);
        }

        static class CuratorCacheListenerBeanPostRegistrar extends AbstractBeanPostProcessorsRegistrar {

            @Override
            protected void doRegisterBeanDefinitions(AnnotationMetadata annotationMetadata,
                                                     BeanDefinitionRegistry registry) {
                Class<CuratorCacheListenerAnnotationBeanPostProcessor> beanClass =
                        CuratorCacheListenerAnnotationBeanPostProcessor.class;

                if (ObjectUtils.isEmpty(
                        this.beanFactory.getBeanNamesForType(beanClass, true, false))) {

                    RootBeanDefinition definition = new RootBeanDefinition(beanClass);
                    definition.setSynthetic(true);
                    registry.registerBeanDefinition("curatorCacheListenerAnnotationBeanPostProcessor", definition);
                }
            }
        }

    }

    private CuratorFrameworkFactory.Builder createCuratorBuilder() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

        if (StringUtils.hasText(properties.getConnectString())) {
            // connection string will be first
            builder.connectString(properties.getConnectString());
        }
        if (StringUtils.hasText(properties.getScheme())
                && StringUtils.hasText(properties.getAuthBase64Str())) {
            builder.authorization(properties.getScheme(),
                    Base64Utils.decodeFromString(properties.getAuthBase64Str()));
        }

        if (properties.getCanBeReadOnly() != null) {
            builder.canBeReadOnly(properties.getCanBeReadOnly());
        }
        if (properties.getUseContainerParentsIfAvailable() != null
                && !properties.getUseContainerParentsIfAvailable()) {
            builder.dontUseContainerParents();
        }
        if (properties.getDefaultDataBase64Str() != null) {
            builder.defaultData(Base64Utils.decodeFromString(properties.getDefaultDataBase64Str()));
        }

        if (StringUtils.hasText(properties.getNamespace())) {
            builder.namespace(properties.getNamespace());
        }

        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(), properties.getMaxRetries());
        builder.retryPolicy(retryPolicy);

        if (null != properties.getSessionTimeOutMs()) {
            builder.sessionTimeoutMs(properties.getSessionTimeOutMs());
        }
        if (null != properties.getConnectionTimeoutMs()) {
            builder.connectionTimeoutMs(properties.getConnectionTimeoutMs());
        }
        if (null != properties.getMaxCloseWaitMs()) {
            builder.maxCloseWaitMs(properties.getMaxCloseWaitMs());
        }

        return builder;
    }
}
