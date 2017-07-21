package com.alice.emily.module.security;

import com.alice.emily.core.Configurer;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 * Created by lianhao on 2017/4/6.
 */
@FunctionalInterface
public interface WebSecurityConfigurer extends Configurer<WebSecurity> {

    @Override
    void configure(WebSecurity web) throws Exception;
}
