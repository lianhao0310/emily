package com.alice.emily.module.grpc;

import java.lang.annotation.*;

/**
 * Created by liupin on 2017/2/21.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GRpcClient {

    String value();
}
