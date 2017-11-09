package com.alice.emily.command;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lianhao on 2017/4/9.
 */
@Data
@ConfigurationProperties(prefix = "emily.command")
public class CommandProperties {

    private boolean enabled = false;
}
