package com.palmaplus.euphoria.module.resteasy;

import com.palmaplus.euphoria.autoconfigure.KeycloakAutoConfiguration;
import com.palmaplus.euphoria.autoconfigure.SecurityExtAutoConfiguration;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

/**
 * Created by liupin on 2017/2/7.
 */
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class,
        SecurityExtAutoConfiguration.class,
        KeycloakSpringBootConfiguration.class,
        KeycloakAutoConfiguration.class }
)
public class ResteasyTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResteasyTestApplication.class, args);
    }
}
