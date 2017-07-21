package com.alice.emily.module.security;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * Created by lianhao on 2017/4/6.
 */
@Data
@ConfigurationProperties(prefix = "security")
public class SecurityExtProperties {

    private boolean permitOptions = true;
    private StaticResource staticResource = new StaticResource();
    private WhiteList whiteList = new WhiteList();
    private List<SecurityConstraint> constraints = Lists.newArrayList();

    public List<SecurityConstraint> getSecurityConstraints() {
        SecurityConstraint staticConstraint = new SecurityConstraint();
        staticConstraint.setName("StaticResource");
        staticConstraint.setAuthRoles(staticResource.roles);
        staticConstraint.setPatterns(staticResource.patterns);

        SecurityConstraint whiteListConstraint = new SecurityConstraint();
        whiteListConstraint.setName("WhiteList");
        whiteListConstraint.setMethods(Lists.newArrayList(HttpMethod.GET, HttpMethod.OPTIONS));
        whiteListConstraint.setPatterns(whiteList.patterns);

        List<SecurityConstraint> securityConstraints = Lists.newArrayList();
        securityConstraints.add(staticConstraint);
        securityConstraints.add(whiteListConstraint);
        securityConstraints.addAll(constraints);
        return securityConstraints;
    }

    @Data
    public static class StaticResource {
        private List<String> patterns = Lists.newArrayList("/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js");
        private boolean authenticated = false;
        private List<String> roles = Lists.newArrayList();
    }

    @Data
    public static class WhiteList {
        private List<String> patterns = Lists.newArrayList("/");
    }

}