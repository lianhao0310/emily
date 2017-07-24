package com.alice.emily.example;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class EmilyExampleAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmilyExampleAdminApplication.class, args);
	}
}