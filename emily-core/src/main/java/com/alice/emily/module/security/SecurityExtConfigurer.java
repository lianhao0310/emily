package com.alice.emily.module.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import java.util.Collection;

/**
 * Created by lianhao on 2017/4/6.
 */
public class SecurityExtConfigurer {

    @Autowired
    private ObjectProvider<Collection<HttpSecurityConfigurer>> httpSecurityConfigurers;
    @Autowired
    private ObjectProvider<Collection<WebSecurityConfigurer>> webSecurityConfigurers;

    private final SecurityExtProperties properties;

    public SecurityExtConfigurer(SecurityExtProperties properties) {
        this.properties = properties;
    }

    public void configure(HttpSecurity http) throws Exception {
        Collection<HttpSecurityConfigurer> configurers = httpSecurityConfigurers.getIfAvailable();
        if (configurers != null && !configurers.isEmpty()) {
            for (HttpSecurityConfigurer configurer : configurers) {
                configurer.configure(http);
            }
        }

        // http option request
        if (properties.isPermitOptions()) {
            http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        }
    }

    public void configure(WebSecurity web) throws Exception {
        Collection<WebSecurityConfigurer> configurers = webSecurityConfigurers.getIfAvailable();
        if (configurers != null && !configurers.isEmpty()) {
            for (WebSecurityConfigurer configurer : configurers) {
                configurer.configure(web);
            }
        }
    }
}
