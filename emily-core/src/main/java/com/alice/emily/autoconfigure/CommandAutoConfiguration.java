package com.alice.emily.autoconfigure;

import com.alice.emily.module.command.BuiltinCommands;
import com.alice.emily.module.command.CommandBeanPostProcessor;
import com.alice.emily.module.command.CommandDispatcher;
import com.alice.emily.module.command.CommandProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lianhao on 2017/2/6.
 */
@Configuration
@ConditionalOnProperty(prefix = "emily.command", name = "enabled")
@EnableConfigurationProperties(CommandProperties.class)
public class CommandAutoConfiguration {

    @Bean
    public BuiltinCommands builtinCommands() {
        return new BuiltinCommands();
    }

    @Bean
    public CommandBeanPostProcessor commandBeanPostProcessor() {
        return new CommandBeanPostProcessor();
    }

    @Bean
    public CommandDispatcher commandDispatcher(CommandBeanPostProcessor postProcessor) {
        return new CommandDispatcher(postProcessor);
    }
}
