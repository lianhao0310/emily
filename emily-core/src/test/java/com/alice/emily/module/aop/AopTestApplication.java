package com.alice.emily.module.aop;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by lianhao on 2017/4/8.
 */
@SpringBootApplication
public class AopTestApplication {

    @Bean
    public AopFeatureTest.AlertIfTestDemo getAlertIfTestDemo() {
        return new AopFeatureTest.AlertIfTestDemo();
    }

    @Bean
    public AopFeatureTest.AuditTestDemo getAuditTestDemo() {
        return new AopFeatureTest.AuditTestDemo();
    }
}