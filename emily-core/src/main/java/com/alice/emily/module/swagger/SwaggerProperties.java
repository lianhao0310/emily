package com.alice.emily.module.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Lianhao on 2017/9/1.
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
}
