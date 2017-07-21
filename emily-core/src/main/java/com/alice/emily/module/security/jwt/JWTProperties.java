package com.alice.emily.module.security.jwt;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.List;

/**
 * Created by liupin on 2017/3/15.
 */
@Data
@ConfigurationProperties(prefix = "emily.jwt")
public class JWTProperties {

    private String issuer = "emily";
    private String header = "Authorization";
    private String secret;
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    private Long expiration = 604800L;
    private String authPath = "/auth";
    private String refreshPath = "/refresh";
    private List<User> users;

    @Data
    public static class User {
        private String username = "emily";
        private String password = "password";
        private List<String> roles = Lists.newArrayList("USER");
        private List<String> authorities = Lists.newArrayList();
        private boolean enabled = true;

        public String[] getAuthorities() {
            HashSet<String> combinedAuthorities = Sets.newHashSet(authorities);
            for (String role : roles) {
                String normalRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                combinedAuthorities.add(normalRole);
            }
            return combinedAuthorities.toArray(new String[combinedAuthorities.size()]);
        }
    }
}
