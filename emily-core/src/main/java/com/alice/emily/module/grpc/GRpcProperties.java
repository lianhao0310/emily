package com.alice.emily.module.grpc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties("emily.grpc")
public class GRpcProperties {

    private Map<String, ClientConfig> clients;
    private ServerConfig server;

    public boolean isServerConfigured() {
        return server != null && server.getPort() != null;
    }

    public boolean isClientConfigured() {
        return clients != null && !clients.isEmpty();
    }

    @Data
    public static class ClientConfig {
        private String host;
        private int port;
    }

    @Data
    public static class ServerConfig {
        private Integer port;
        private boolean daemon;
    }
}