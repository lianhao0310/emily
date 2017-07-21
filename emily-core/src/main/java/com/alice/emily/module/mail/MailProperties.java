package com.alice.emily.module.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by liupin on 2017/1/8.
 */
@Data
@ConfigurationProperties(prefix = "emily.mail")
public class MailProperties {

    private boolean enabled = true;
    private MailServerProperties server;
    private Map<String, MailGroupProperties> groups;

    @Data
    public static class MailServerProperties {
        private String host;
        private Integer port;
        private boolean ssl = false;
        private String username;
        private String password;
    }

    @Data
    public static class MailGroupProperties {
        private String from;
        private List<String> to;
        private List<String> cc;
        private String template;
    }
}
