package com.alice.emily.autoconfigure;

import com.alice.emily.command.BuiltinCommands;
import com.alice.emily.command.CommandAnnotationBeanPostProcessor;
import com.alice.emily.command.CommandDispatcher;
import com.alice.emily.command.CommandProperties;
import com.alice.emily.core.AbstractBeanPostProcessorsRegistrar;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ObjectUtils;

/**
 * Created by lianhao on 2017/2/6.
 */
@Configuration
@ConditionalOnProperty(prefix = "emily.command", name = "enabled")
@Import(CommandAutoConfiguration.CommandAnnotationBeanPostProcessorRegistrar.class)
@EnableConfigurationProperties(CommandProperties.class)
public class CommandAutoConfiguration {

    @Bean
    public BuiltinCommands builtinCommands() {
        return new BuiltinCommands();
    }

    @Bean
    public CommandDispatcher commandDispatcher(ThreadPoolTaskExecutor executor) {
        return new CommandDispatcher(executor);
    }

    static class CommandAnnotationBeanPostProcessorRegistrar
            extends AbstractBeanPostProcessorsRegistrar {

        @Override
        protected void doRegisterBeanDefinitions(AnnotationMetadata annotationMetadata,
                                                 BeanDefinitionRegistry registry) {
            Class<CommandAnnotationBeanPostProcessor> beanClass = CommandAnnotationBeanPostProcessor.class;
            if (ObjectUtils.isEmpty(
                    this.beanFactory.getBeanNamesForType(beanClass, true, false))) {
                RootBeanDefinition definition = new RootBeanDefinition(beanClass);
                definition.setSynthetic(true);
                registry.registerBeanDefinition("commandAnnotationBeanPostProcessor", definition);
            }
        }
    }
}
