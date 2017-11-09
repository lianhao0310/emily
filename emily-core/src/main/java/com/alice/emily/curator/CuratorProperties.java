package com.alice.emily.curator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lianhao on 2017/6/13.
 */
@Data
@ConfigurationProperties(prefix = "emily.curator")
public class CuratorProperties {

    private String scheme;
    private String authBase64Str;
    private Boolean canBeReadOnly;
    private Boolean useContainerParentsIfAvailable;

    /**
     * list of servers to connect to
     */
    private String connectString;

    private String defaultDataBase64Str;
    private String namespace;


    private Integer sessionTimeOutMs;
    private Integer connectionTimeoutMs;
    private Integer maxCloseWaitMs;

    /**
     * initial amount of time to wait between retries
     */
    private int baseSleepTimeMs = 1000;
    private int maxRetries = 5;
}
