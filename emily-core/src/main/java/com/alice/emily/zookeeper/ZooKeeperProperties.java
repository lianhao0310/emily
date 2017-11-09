package com.alice.emily.zookeeper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lianhao on 2017/6/26.
 */
@Data
@ConfigurationProperties(prefix = "emily.zookeeper")
public class ZooKeeperProperties {

    private Zookeeper embedded = new Zookeeper();

    @Data
    public static class Zookeeper {
        private boolean enabled = false;
        private Integer port = 2181;
        private String dataDir;
        private String dataLogDir;
        private Integer tickTime = 3000;
        private Integer minSessionTimeout = -1;
        private Integer maxSessionTimeout = -1;
    }
}
