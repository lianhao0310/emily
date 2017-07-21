package com.alice.emily.module.security.keycloak;

import com.alice.emily.module.security.SecurityExtConfigurer;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by lianhao on 2017/4/5.
 */
@Configuration
@ConditionalOnWebApplication
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class KeycloakSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    private final ObjectProvider<SecurityExtConfigurer> configurer;

    public KeycloakSecurityConfiguration(ObjectProvider<SecurityExtConfigurer> configurer) {
        this.configurer = configurer;
    }

    // Registers the KeycloakAuthenticationProvider with the authentication manager.
    @Bean
    public GlobalAuthenticationConfigurerAdapter globalAuthenticationConfigurerAdapter() {
        return new GlobalAuthenticationConfigurerAdapter() {
            @Override
            public void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.authenticationProvider(keycloakAuthenticationProvider());
            }
        };
    }

    @Override
    protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        KeycloakAuthenticationProvider provider = new KeycloakAuthenticationProvider();
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setConvertToUpperCase(true);
        mapper.setDefaultAuthority("ROLE_USER");
        provider.setGrantedAuthoritiesMapper(mapper);
        return provider;
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    // Make sure Spring Security Adapter looks at the configuration provided by the Spring Boot Adapter
    @Bean
    @ConditionalOnMissingBean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    // Spring Boot attempts to eagerly register filter beans with the web application context.
    // Therefore, when running the Keycloak Spring Security adapter in a Spring Boot environment,
    // it may be necessary to add two FilterRegistrationBeans to your security configuration
    // to prevent the Keycloak filters from being registered twice.
    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public KeycloakSecurityConstraintConfigurer keycloakSecurityConstraintConfigurer(
            KeycloakSpringBootProperties properties) {
        return new KeycloakSecurityConstraintConfigurer(properties);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .sessionManagement()
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                .and()
                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler())
                .logoutUrl("/sso/logout")
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers("/sso/logout", "/").permitAll()
                .anyRequest().permitAll();

        SecurityExtConfigurer configurer = this.configurer.getIfAvailable();
        if (configurer != null) {
            configurer.configure(httpSecurity);
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        SecurityExtConfigurer configurer = this.configurer.getIfAvailable();
        if (configurer != null) {
            configurer.configure(web);
        }
    }
}
