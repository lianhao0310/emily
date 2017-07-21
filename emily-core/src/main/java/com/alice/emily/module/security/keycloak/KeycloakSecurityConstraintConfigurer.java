package com.alice.emily.module.security.keycloak;

import com.alice.emily.module.security.HttpSecurityConfigurer;
import com.alice.emily.module.security.HttpSecurityConstraintConfigurer;
import com.alice.emily.module.security.SecurityConstraint;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by lianhao on 2017/4/11.
 */
public class KeycloakSecurityConstraintConfigurer implements HttpSecurityConfigurer {

    private final KeycloakSpringBootProperties properties;

    public KeycloakSecurityConstraintConfigurer(KeycloakSpringBootProperties properties) {
        this.properties = properties;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<SecurityConstraint> securityCollections = properties.getSecurityConstraints()
                .stream()
                .map(KeycloakSpringBootProperties.SecurityConstraint::getSecurityCollections)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(this::mapTo)
                .collect(Collectors.toList());
        new HttpSecurityConstraintConfigurer(securityCollections).configure(http);
    }

    private SecurityConstraint mapTo(KeycloakSpringBootProperties.SecurityCollection securityCollection) {
        SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setName(securityCollection.getName());
        securityConstraint.setDescription(securityCollection.getDescription());
        securityConstraint.setPatterns(securityCollection.getPatterns());
        securityConstraint.setAuthRoles(securityCollection.getAuthRoles());

        if (!securityCollection.getMethods().isEmpty()) {
            List<HttpMethod> methods = securityCollection.getMethods()
                    .stream()
                    .map(String::toUpperCase)
                    .map(HttpMethod::valueOf)
                    .collect(Collectors.toList());
            securityConstraint.setMethods(methods);
        }

        if (!securityCollection.getOmittedMethods().isEmpty()) {
            List<HttpMethod> omittedMethods = securityCollection.getOmittedMethods()
                    .stream()
                    .map(String::toUpperCase)
                    .map(HttpMethod::valueOf)
                    .collect(Collectors.toList());
            securityConstraint.setOmittedMethods(omittedMethods);
        }
        return securityConstraint;
    }
}
