package com.alice.emily.resteasy;

import com.alice.emily.resteasy.resource.RestResourceConfiguration;
import com.alice.emily.test.TestPlainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by lianhao on 2017/2/7.
 */
@TestPlainConfiguration
@ComponentScan(basePackageClasses = RestResourceConfiguration.class)
public class ResteasyTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResteasyTestApplication.class, args);
    }
}
