package com.alice.emily.module.security;

import com.alice.emily.core.Configurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Created by lianhao on 2017/4/6.
 */
@FunctionalInterface
public interface HttpSecurityConfigurer extends Configurer<HttpSecurity> {

    @Override
    void configure(HttpSecurity http) throws Exception;
}
