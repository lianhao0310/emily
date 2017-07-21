package com.alice.emily.module.security.keycloak;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * Created by liupin on 2017/4/6.
 */
@Configuration
@ConditionalOnClass({ Keycloak.class, ResteasyClient.class })
@EnableConfigurationProperties(KeycloakClientProperties.class)
public class KeycloakClientConfiguration {

    private final KeycloakClientProperties properties;

    public KeycloakClientConfiguration(KeycloakClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnBean(KeycloakClientRequestFactory.class)
    public KeycloakRestTemplate keycloakRestTemplate(KeycloakClientRequestFactory keycloakClientRequestFactory) {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "emily.keycloak.admin", name = "serverUrl")
    public Keycloak keycloak(ResteasyClient resteasyClient) {
        KeycloakClientProperties.AdminClientProperties admin = properties.getAdmin();
        return KeycloakBuilder.builder()
                .serverUrl(admin.getServerUrl())
                .realm(admin.getRealm())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .grantType(admin.getGrantType())
                .clientId(admin.getClientId())
                .clientSecret(admin.getClientSecret())
                .resteasyClient(resteasyClient)
                .build();
    }
}
