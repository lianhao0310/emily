package com.alice.emily.autoconfigure;

import com.alice.emily.module.security.HttpSecurityConstraintConfigurer;
import com.alice.emily.module.security.SecurityConstraint;
import com.alice.emily.module.security.SecurityExtConfigurer;
import com.alice.emily.module.security.SecurityExtProperties;
import com.alice.emily.module.security.principal.SecurityPrincipalConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.List;

/**
 * Created by liupin on 2017/4/6.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ HttpSecurity.class, WebSecurity.class })
@EnableConfigurationProperties(SecurityExtProperties.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityPrincipalConfiguration.class)
public class SecurityExtAutoConfiguration {

    private final SecurityExtProperties properties;

    public SecurityExtAutoConfiguration(SecurityExtProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityExtConfigurer securityExtConfigurer() {
        return new SecurityExtConfigurer(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpSecurityConstraintConfigurer httpSecurityConstraintConfigurer() {
        List<SecurityConstraint> securityConstraints = properties.getSecurityConstraints();
        return new HttpSecurityConstraintConfigurer(securityConstraints);
    }

    @Order(Ordered.LOWEST_PRECEDENCE - 1000)
    @Configuration
    @ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
    public static class DefaultSecurityExtConfiguration extends WebSecurityConfigurerAdapter {

        private final SecurityExtConfigurer securityExtConfigurer;

        public DefaultSecurityExtConfiguration(SecurityExtConfigurer securityExtConfigurer) {
            this.securityExtConfigurer = securityExtConfigurer;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            securityExtConfigurer.configure(http);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            securityExtConfigurer.configure(web);
        }
    }
}
