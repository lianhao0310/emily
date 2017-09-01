package com.alice.emily.example.okhttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmilyExampleSlf4jLog4j2Application {

	private static final Logger logger = LoggerFactory.getLogger(EmilyExampleSlf4jLog4j2Application.class);

	public static void main(String[] args) {
		SpringApplication.run(EmilyExampleSlf4jLog4j2Application.class, args);
		logger.info("Begin log after running EmilyExampleSlf4jLog4j2Application");
		try {
			int i= 1/0;
		}catch (Exception e){
			logger.error("Has error:",e);
		}
	}
}
