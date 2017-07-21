package com.alice.emily.autoconfigure;

import com.alice.emily.core.BeanFactoryProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;

/**
 * Created by lianhao on 2017/2/6.
 */
@Configuration
public class ProviderAutoConfiguration {

    @Bean
    public static BeanFactoryProvider getApplicationContextProvider() {
        return new BeanFactoryProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public BeanResolver beanResolver(ApplicationContext applicationContext) {
        return new BeanFactoryResolver(applicationContext.getAutowireCapableBeanFactory());
    }
}
