package com.palmaplus.euphoria.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by liupin on 2017/2/6.
 */
@SpringBootApplication
public class CoreTestApplication {

    @Bean
    public AsyncFeatureTest.AsyncTestDemo getAsyncTestDemo() {
        return new AsyncFeatureTest.AsyncTestDemo();
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreTestApplication.class, args);
    }
}
