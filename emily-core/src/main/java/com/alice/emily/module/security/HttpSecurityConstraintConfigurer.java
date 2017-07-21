package com.alice.emily.module.security;

import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.List;
import java.util.Set;

/**
 * Created by lianhao on 2017/4/11.
 */
public class HttpSecurityConstraintConfigurer implements HttpSecurityConfigurer {

    private final List<SecurityConstraint> securityConstraints;

    public HttpSecurityConstraintConfigurer(List<SecurityConstraint> securityConstraints) {
        this.securityConstraints = securityConstraints;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (securityConstraints == null || securityConstraints.isEmpty()) return;
        for (SecurityConstraint constraint : securityConstraints) {
            if (constraint == null || constraint.getPatterns().isEmpty()) continue;
            String[] patterns = constraint.getPatterns().toArray(new String[constraint.getPatterns().size()]);
            Sets.SetView<HttpMethod> unionMethods = getHttpMethods(constraint);
            if (unionMethods.size() == HttpMethod.values().length) {
                doConfigure(http, constraint, patterns, null);
            } else {
                for (HttpMethod method : unionMethods) {
                    doConfigure(http, constraint, patterns, method);
                }
            }
        }
    }

    private Sets.SetView<HttpMethod> getHttpMethods(SecurityConstraint constraint) {
        Set<HttpMethod> all = Sets.newHashSet(HttpMethod.values());
        Set<HttpMethod> methods = Sets.newHashSet(constraint.getMethods());
        Set<HttpMethod> omittedMethods = Sets.newHashSet(constraint.getOmittedMethods());
        return Sets.union(methods, Sets.difference(all, omittedMethods));
    }

    private void doConfigure(HttpSecurity http, SecurityConstraint constraint, String[] patterns, HttpMethod method) throws Exception {
        String[] roles = new String[0];

        if (constraint.getAuthRoles() != null && !constraint.getAuthRoles().isEmpty()) {
            roles = constraint.getAuthRoles().toArray(new String[constraint.getAuthRoles().size()]);
        }

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl matchers;

        if (method != null) {
            matchers = http.authorizeRequests().antMatchers(method, patterns);
        } else {
            matchers = http.authorizeRequests().antMatchers(patterns);
        }

        if (roles.length > 0) {
            matchers.hasAnyRole(roles);
        } else {
            matchers.authenticated();
        }
    }
}
