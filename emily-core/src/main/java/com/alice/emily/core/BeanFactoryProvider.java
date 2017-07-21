package com.alice.emily.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Created by liupin on 2017/2/6.
 */
public class BeanFactoryProvider implements BeanFactoryPostProcessor {

    private static ListableBeanFactory beanFactory;

    public static ListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanFactoryProvider.beanFactory = beanFactory;
    }
}
