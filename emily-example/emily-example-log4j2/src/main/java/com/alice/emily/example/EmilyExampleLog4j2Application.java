package com.alice.emily.example;

import com.alice.emily.utils.LOG;
import com.alice.emily.utils.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmilyExampleLog4j2Application {

    private static Logger logger = LOG.getLogger(EmilyExampleLog4j2Application.class);

    public static void main(String[] args) {
        SpringApplication.run(EmilyExampleLog4j2Application.class, args);
        logger.info("Begin log after running");
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            logger.error("Has error:", e);
        }
    }
}
