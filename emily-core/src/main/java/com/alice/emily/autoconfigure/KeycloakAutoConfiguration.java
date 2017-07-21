package com.alice.emily.autoconfigure;

import com.alice.emily.module.security.keycloak.KeycloakClientConfiguration;
import com.alice.emily.module.security.keycloak.KeycloakSecurityConfiguration;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfiguration;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by lianhao on 2017/4/9.
 */
@Configuration
@ConditionalOnClass({ Keycloak.class })
@AutoConfigureBefore({ SecurityExtAutoConfiguration.class, JWTAutoConfiguration.class })
@AutoConfigureAfter(KeycloakSpringBootConfiguration.class)
@Import({ KeycloakClientConfiguration.class, KeycloakSecurityConfiguration.class })
public class KeycloakAutoConfiguration {
}
