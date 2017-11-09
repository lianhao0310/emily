package com.alice.emily.resteasy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lianhao on 2017/3/27.
 */
@Data
@ConfigurationProperties("emily.resteasy")
public class ResteasyProperties {

    private boolean httpCache = false;

}
