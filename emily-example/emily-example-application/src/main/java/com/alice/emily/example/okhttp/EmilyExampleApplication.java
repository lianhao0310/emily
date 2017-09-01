package com.alice.emily.example.okhttp;

import com.alice.emily.example.okhttp.domain.Customer;
import com.alice.emily.example.okhttp.domain.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmilyExampleApplication {

    @Bean
    public CommandLineRunner customers(CustomerRepository repository) {
        return args -> {
            repository.save(new Customer("Jack", "Bauer"));
            repository.save(new Customer("Chloe", "O'Brian"));
            repository.save(new Customer("Kim", "Bauer"));
            repository.save(new Customer("David", "Palmer"));
            repository.save(new Customer("Michelle", "Dessler"));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(EmilyExampleApplication.class, args);
    }
}
