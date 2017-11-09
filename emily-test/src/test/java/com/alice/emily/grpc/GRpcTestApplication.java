package com.alice.emily.grpc;

import com.alice.emily.test.TestPlainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by lianhao on 2017/7/28.
 */
@TestPlainConfiguration
@ComponentScan
public class GRpcTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GRpcTestApplication.class, args);
    }

}
