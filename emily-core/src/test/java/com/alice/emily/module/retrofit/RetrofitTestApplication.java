package com.palmaplus.euphoria.module.retrofit;

import com.palmaplus.euphoria.autoconfigure.KeycloakAutoConfiguration;
import com.palmaplus.euphoria.autoconfigure.SecurityExtAutoConfiguration;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

/**
 * Created by liupin on 2017/3/31.
 */
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class,
        SecurityExtAutoConfiguration.class,
        KeycloakSpringBootConfiguration.class,
        KeycloakAutoConfiguration.class }
)
public class RetrofitTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetrofitTestApplication.class, args);
    }
}
