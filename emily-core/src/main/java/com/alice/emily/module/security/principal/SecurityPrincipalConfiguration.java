package com.alice.emily.module.security.principal;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.BeanResolver;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.ext.MessageBodyReader;

/**
 * Created by lianhao on 2017/4/11.
 */
@Configuration
@ConditionalOnClass({ SecurityContextHolder.class, EnableWebSecurity.class })
public class SecurityPrincipalConfiguration {

    @Configuration
    @ConditionalOnClass(MessageBodyReader.class)
    public static class SecurityConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public AuthenticationPrincipalReader authenticationPrincipalReader(BeanResolver beanResolver) {
            return new AuthenticationPrincipalReader(beanResolver);
        }
    }
}
