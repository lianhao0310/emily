package com.alice.emily.autoconfigure.core;

import com.alice.emily.aop.AlertIfAspect;
import com.alice.emily.aop.AopProperties;
import com.alice.emily.aop.AuditAspect;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by lianhao on 2017/2/6.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ConditionalOnClass(Aspect.class)
@EnableConfigurationProperties(AopProperties.class)
public class AopAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "emily.aop.alert-if", name = "enabled", matchIfMissing = true)
    public AlertIfAspect alertIfAspect() {
        return new AlertIfAspect();
    }

    @Bean
    @ConditionalOnProperty(prefix = "emily.aop.audit", name = "enabled")
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }

}
