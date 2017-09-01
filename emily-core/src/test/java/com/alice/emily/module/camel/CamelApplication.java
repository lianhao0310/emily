package com.alice.emily.module.camel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Created by lianhao on 2017/2/10.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
public class CamelApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamelApplication.class, args);
    }
}
