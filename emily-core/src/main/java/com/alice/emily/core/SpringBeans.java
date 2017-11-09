package com.alice.emily.core;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.util.ObjectUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by lianhao on 2017/2/6.
 */
@ParametersAreNonnullByDefault
@UtilityClass
public class SpringBeans {

    private static ListableBeanFactory getBeanFactory() {
        return SpringStuffCollector.getBeanFactory();
    }

    public static Object getBean(String name) {
        try {
            return getBeanFactory().getBean(name);
        } catch (Throwable t) {
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        try {
            return getBeanFactory().getBean(name, requiredType);
        } catch (Throwable t) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> requiredType) {
        try {
            return getBeanFactory().getBean(requiredType);
        } catch (Throwable t) {
            return null;
        }
    }

    public static Object getBean(String name, Object... args) {
        try {
            return getBeanFactory().getBean(name, args);
        } catch (Throwable t) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) {
        try {
            return getBeanFactory().getBean(requiredType, args);
        } catch (Throwable t) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> requiredType, String qualifier) {
        try {
            return BeanFactoryAnnotationUtils.qualifiedBeanOfType(getBeanFactory(), requiredType, qualifier);
        } catch (Throwable t) {
            return null;
        }
    }

    public static <T> List<T> getBeans(Class<T> type) {
        try {
            Collection<T> values = BeanFactoryUtils.beansOfTypeIncludingAncestors(getBeanFactory(), type).values();
            return Lists.newArrayList(values);
        } catch (Throwable t) {
            return null;
        }
    }

    public static BeanDefinition getBeanDefinition(String beanName) {
        ListableBeanFactory beanFactory = getBeanFactory();
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            return ((ConfigurableListableBeanFactory) beanFactory).getBeanDefinition(beanName);
        }
        return null;
    }

    public static boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isSingleton(name);
    }

    public static boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isPrototype(name);
    }

    public static boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isTypeMatch(name, typeToMatch);
    }

    public static boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isTypeMatch(name, typeToMatch);
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getType(name);
    }

    public static String[] getAliases(String name) {
        return getBeanFactory().getAliases(name);
    }

    public static <T> String[] getBeanNames(Class<T> beanType) {
        return getBeanFactory().getBeanNamesForType(beanType);
    }

    public static <T, A extends Annotation> List<String> getBeanNames(Class<T> beanType, Class<A> annotationType) {
        String[] names = getBeanFactory().getBeanNamesForType(beanType);
        if (ObjectUtils.isEmpty(names)) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        for (String name : names) {
            BeanDefinition beanDefinition = getBeanDefinition(name);
            if (beanDefinition != null && beanDefinition.getSource() != null
                    && beanDefinition.getSource() instanceof StandardMethodMetadata) {
                StandardMethodMetadata metadata = (StandardMethodMetadata) beanDefinition.getSource();
                if (metadata.isAnnotated(annotationType.getName())) {
                    result.add(name);
                    continue;
                }
            }
            if (null != findAnnotationOnBean(name, annotationType)) {
                result.add(name);
            }
        }
        return result;
    }

    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return getBeanFactory().findAnnotationOnBean(beanName, annotationType);
    }

}
