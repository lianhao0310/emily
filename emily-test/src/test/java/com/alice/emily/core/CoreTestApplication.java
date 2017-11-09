package com.alice.emily.core;

import com.alice.emily.test.TestPlainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by lianhao on 2017/2/6.
 */
@TestPlainConfiguration
public class CoreTestApplication {

    @Bean
    public AopFeatureTest.AlertIfTestDemo getAlertIfTestDemo() {
        return new AopFeatureTest.AlertIfTestDemo();
    }

    @Bean
    public AopFeatureTest.AuditTestDemo getAuditTestDemo() {
        return new AopFeatureTest.AuditTestDemo();
    }

    @Bean
    public AsyncFeatureTest.AsyncTestDemo asyncTestDemo() {
        return new AsyncFeatureTest.AsyncTestDemo();
    }

    @Bean
    public JCacheFeatureTest.JCacheTestDemo jCacheTestDemo() {
        return new JCacheFeatureTest.JCacheTestDemo();
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreTestApplication.class, args);
    }
}
