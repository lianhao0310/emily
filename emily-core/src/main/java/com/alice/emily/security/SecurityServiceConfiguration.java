package com.alice.emily.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lianhao on 2017/4/17.
 */
@Configuration
public class SecurityServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "security.mapper.simple")
    public SimpleRoleGrantedAuthorityMapper simpleRoleGrantedAuthorityMapper() {
        return new SimpleRoleGrantedAuthorityMapper();
    }
}
