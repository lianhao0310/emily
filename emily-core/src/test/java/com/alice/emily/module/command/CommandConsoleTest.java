package com.palmaplus.euphoria.module.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by liupin on 2017/3/6.
 */
@SpringBootApplication
public class CommandConsoleTest extends CommandConsole {

    public CommandConsoleTest() {
        super("Euphoria CMD >>> ", "test");
    }

    public static void main(String[] args) {
        System.setProperty("euphoria.command.enabled", "true");
        ConfigurableApplicationContext context = SpringApplication.run(CommandConsoleTest.class);
        context.getBean(CommandConsoleTest.class).run();
    }

}
