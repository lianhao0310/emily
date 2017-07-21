package com.alice.emily.module.command;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by liupin on 2017/4/9.
 */
@Data
@ConfigurationProperties(prefix = "emily.command")
public class CommandProperties {

    private boolean enabled = false;
}
