package com.alice.emily.autoconfigure.core;

import com.alice.emily.core.AbstractBeanPostProcessorsRegistrar;
import com.alice.emily.core.SpringStuffCollector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.expression.BeanResolver;

import javax.annotation.Nonnull;

/**
 * Created by lianhao on 2017/2/6.
 */
@Configuration
@PropertySource(value = { "classpath:application-eu-default.properties" }, ignoreResourceNotFound = true)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import(BeanResolverAutoConfiguration.BeanPostProcessorsRegistrar.class)
public class BeanResolverAutoConfiguration implements ApplicationContextAware, EnvironmentAware {

    private ApplicationContext applicationContext;
    private Environment environment;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        SpringStuffCollector.setBeanFactory(applicationContext);
    }

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        this.environment = environment;
        SpringStuffCollector.setEnvironment(environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public BeanResolver beanResolver() {
        return new BeanFactoryResolver(applicationContext.getAutowireCapableBeanFactory());
    }

    public static class BeanPostProcessorsRegistrar extends AbstractBeanPostProcessorsRegistrar {

        @Override
        protected void doRegisterBeanDefinitions(AnnotationMetadata annotationMetadata,
                                                 BeanDefinitionRegistry registry) {
            SpringStuffCollector.setBeanFactory(beanFactory);
            SpringStuffCollector.setEnvironment(environment);
        }

    }
}
