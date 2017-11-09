package com.alice.emily.swagger;

import com.alice.emily.core.SpringContext;
import com.alice.emily.utils.Network;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * Created by lianhao on 2017/6/12.
 */
@Data
@ConfigurationProperties(prefix = "emily.swagger")
public class SwaggerProperties {

    private boolean enabled = false;

    private String title;
    private String version = "1.0.0";
    private String description;
    private String license;
    private String licenseUrl;

    private String host;
    private String path;

    public String determineHost() {
        if (StringUtils.isEmpty(host)) {
            Integer port = SpringContext.serverPort();
            String localhostStr = Network.getLocalhostStr();
            if (port == 80) {
                this.host = localhostStr;
            } else {
                this.host = Network.buildInetSocketAddress(localhostStr, port).toString();
            }
        }
        return this.host;
    }

    public String determinePath() {
        if (StringUtils.isEmpty(path)) {
            this.path = SpringContext.baseUrl();
        }
        return this.path;
    }

}
