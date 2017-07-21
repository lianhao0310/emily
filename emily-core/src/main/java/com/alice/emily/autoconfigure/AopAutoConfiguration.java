package com.alice.emily.autoconfigure;

import com.alice.emily.module.aop.AlertIfAspect;
import com.alice.emily.module.aop.AuditAspect;
import com.alice.emily.module.mail.MailSender;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by liupin on 2017/2/6.
 */
@Configuration
@AutoConfigureAfter(MailAutoConfiguration.class)
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ConditionalOnClass(Aspect.class)
public class AopAutoConfiguration {

    @Bean
    @ConditionalOnBean(MailSender.class)
    public AlertIfAspect getAlertIfAspect(MailSender sender) {
        return new AlertIfAspect(sender);
    }

    @Bean
    public AuditAspect getAuditAspect() {
        return new AuditAspect();
    }
}
