package com.alice.emily.module.security.jwt;

import com.alice.emily.autoconfigure.KeycloakAutoConfiguration;
import com.alice.emily.module.security.jwt.resource.InsecureResource;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import(CustomClaimConfiguration.class)
@EnableAutoConfiguration(exclude = { KeycloakSpringBootConfiguration.class, KeycloakAutoConfiguration.class })
@ComponentScan(basePackageClasses = InsecureResource.class)
public class JWTTestApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "jwt");
        SpringApplication.run(JWTTestApplication.class, args);
    }
}
