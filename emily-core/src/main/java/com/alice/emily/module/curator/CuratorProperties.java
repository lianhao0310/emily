package com.alice.emily.module.curator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Lianhao on 2017/8/21.
 */
@Data
@ConfigurationProperties(prefix = "emily.curator")
public class CuratorProperties {
    private String aclProviderRef;

    private String scheme;

    private String authBase64Str;

    private String authInfosRef;

    private Boolean canBeReadOnly;

    private Boolean useContainerParentsIfAvailable;

    private String compressionProviderRef;

    private String ensembleProviderRef;

    /**
     * list of servers to connect to
     */
    private String connectString;

    private String defaultDataBase64Str;

    private String namespace;

    /**
     * session timeout
     */
    private Integer sessionTimeOutMs;

    /**
     * connection timeout
     */
    private Integer connectionTimeoutMs;

    private Integer maxCloseWaitMs;

    private String threadFactoryRef;

    private String zookeeperFactoryRef;

    /**
     * initial amount of time to wait between retries
     */
    private int baseSleepTimeMs = 1 * 1000;

    /**
     * max number of times to retry
     */
    private int maxRetries = 5;
}
