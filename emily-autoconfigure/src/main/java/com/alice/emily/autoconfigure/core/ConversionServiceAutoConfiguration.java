package com.alice.emily.autoconfigure.core;

import com.alice.emily.autoconfigure.CuratorAutoConfiguration;
import com.alice.emily.convert.ConversionServiceConfigurerInitializer;
import com.alice.emily.convert.DefaultConversionServiceConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

/**
 * Created by lianhao on 2017/6/13.
 */
@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@AutoConfigureBefore(CuratorAutoConfiguration.class)
public class ConversionServiceAutoConfiguration {

    @Bean(name = AbstractApplicationContext.CONVERSION_SERVICE_BEAN_NAME)
    @ConditionalOnMissingBean(FormattingConversionService.class)
    public FormattingConversionServiceFactoryBean formattingConversionService() {
        return new FormattingConversionServiceFactoryBean();
    }

    @Bean
    public DefaultConversionServiceConfigurer conversionServiceConfigurer() {
        return new DefaultConversionServiceConfigurer();
    }

    @Bean
    public ConversionServiceConfigurerInitializer conversionServiceConfigurerInitializer() {
        return new ConversionServiceConfigurerInitializer();
    }

}
