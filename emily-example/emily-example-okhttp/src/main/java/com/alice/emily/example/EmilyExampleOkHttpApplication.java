package com.alice.emily.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class EmilyExampleOkHttpApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmilyExampleOkHttpApplication.class, args);
    }
}
