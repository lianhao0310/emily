package com.alice.emily.module.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by lianhao on 2017/3/6.
 */
@SpringBootApplication
public class CommandConsoleTest extends CommandConsole {

    public CommandConsoleTest() {
        super("Emily CMD >>> ", "test");
    }

    public static void main(String[] args) {
        System.setProperty("emily.command.enabled", "true");
        ConfigurableApplicationContext context = SpringApplication.run(CommandConsoleTest.class);
        context.getBean(CommandConsoleTest.class).run();
    }

}
