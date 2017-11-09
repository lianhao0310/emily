package com.alice.emily.web;

import com.alice.emily.test.TestPlainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by lianhao on 2017/7/28.
 */
@TestPlainConfiguration
@ComponentScan
public class WebTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebTestApplication.class, args);
    }

}
