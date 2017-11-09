package com.alice.emily.core.command;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lianhao on 2017/2/6.
 */
@Configuration
public class CommandTestConfiguration {

    @Bean
    public GreetCommands getGreetCommands() {
        return new GreetCommands();
    }

    @Bean
    public TestCommands getTestCommands() {
        return new TestCommands();
    }
}
