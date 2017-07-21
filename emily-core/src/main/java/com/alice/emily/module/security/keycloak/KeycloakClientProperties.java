package com.alice.emily.module.security.keycloak;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lianhao on 2017/4/6.
 */
@Data
@ConfigurationProperties(prefix = "emily.keycloak")
public class KeycloakClientProperties {

    private AdminClientProperties admin = new AdminClientProperties();

    @Data
    public static class AdminClientProperties {
        private String serverUrl;
        private String realm;
        private String username;
        private String password;
        private String clientId;
        private String clientSecret;
        private String grantType;
        private String authorization;
    }
}
