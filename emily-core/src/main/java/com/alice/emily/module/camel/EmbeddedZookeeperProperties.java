package com.alice.emily.module.camel;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lianhao on 2017/3/1.
 */
@Data
@ConfigurationProperties(prefix = "emily.zookeeper.embedded")
public class EmbeddedZookeeperProperties {
    private boolean enabled = false;
    private int port = 2181;
}
