package com.alice.emily.module.resteasy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by liupin on 2017/3/27.
 */
@Data
@ConfigurationProperties("emily.resteasy")
public class ResteasyProperties {

    private boolean cors = true;
    private boolean httpCache = true;

}
