package com.alice.emily.core;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.env.Environment;

/**
 * Created by lianhao on 2017/2/6.
 */
public class SpringStuffCollector {

    private static volatile ListableBeanFactory beanFactory;

    private static volatile Environment environment;

    public static ListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static void setBeanFactory(ListableBeanFactory beanFactory) {
        SpringStuffCollector.beanFactory = beanFactory;
    }

    public static Environment getEnvironment() {
        return environment;
    }

    public static void setEnvironment(Environment environment) {
        SpringStuffCollector.environment = environment;
    }

}
