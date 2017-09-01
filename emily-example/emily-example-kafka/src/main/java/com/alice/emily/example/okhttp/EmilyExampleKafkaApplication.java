package com.alice.emily.example.okhttp;

import com.alice.emily.example.okhttp.Message.Sender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EmilyExampleKafkaApplication{
	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(EmilyExampleKafkaApplication.class, args);
		while (true) {
			Sender sender = app.getBean(Sender.class);
			sender.sendMessage();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
