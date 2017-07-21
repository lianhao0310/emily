package com.alice.emily.core;

import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.core.ResolvableType;

import java.util.Collection;
import java.util.List;

/**
 * Created by liupin on 2017/2/6.
 */
public class BeanProvider {

    private BeanProvider() {
    }

    private static ListableBeanFactory getBeanFactory() {
        return BeanFactoryProvider.getBeanFactory();
    }

    public static Object getBean(String name) {
        try {
            return getBeanFactory().getBean(name);
        } catch (BeansException e) {
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        try {
            return getBeanFactory().getBean(name, requiredType);
        } catch (BeansException e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> requiredType) {
        try {
            return getBeanFactory().getBean(requiredType);
        } catch (BeansException e) {
            return null;
        }
    }

    public static Object getBean(String name, Object... args) {
        try {
            return getBeanFactory().getBean(name, args);
        } catch (BeansException e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) {
        try {
            return getBeanFactory().getBean(requiredType, args);
        } catch (BeansException e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> requiredType, String qualifier) {
        try {
            return BeanFactoryAnnotationUtils.qualifiedBeanOfType(getBeanFactory(), requiredType, qualifier);
        } catch (BeansException e) {
            return null;
        }
    }

    public static <T> List<T> getBeans(Class<T> type) {
        Collection<T> values = BeanFactoryUtils.beansOfTypeIncludingAncestors(getBeanFactory(), type).values();
        return Lists.newArrayList(values);
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
}
