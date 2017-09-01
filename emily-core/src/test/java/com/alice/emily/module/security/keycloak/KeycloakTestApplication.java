package com.alice.emily.module.security.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by lianhao on 2017/4/5.
 */
@SpringBootApplication
public class KeycloakTestApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "keycloak");
        SpringApplication.run(KeycloakTestApplication.class, args);
    }
}
