package com.alice.emily.aop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lianhao on 2017/6/27.
 */
@Data
@ConfigurationProperties(prefix = "emily.aop")
public class AopProperties {

    private Audit audit = new Audit();

    private AlertIf alertIf = new AlertIf();

    @Data
    public static class Audit {
        private boolean enabled = false;
    }

    @Data
    public static class AlertIf {
        private boolean enabled = true;
    }

}
