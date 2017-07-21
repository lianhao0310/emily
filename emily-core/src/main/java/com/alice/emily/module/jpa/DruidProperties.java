package com.alice.emily.module.jpa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lianhao on 2017/3/2.
 */
@Data
@ConfigurationProperties(prefix = "emily.druid.stat")
public class DruidProperties {
    private boolean enable = false;
    private String[] mappings = new String[]{ "/druid/*" };
    private String allow = "127.0.0.1";
    private String deny;
    private String username = "emily";
    private String password = "druid";
    private boolean resetEnable = false;
}
