package com.alice.emily.template.thymeleaf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Created by lianhao on 2017/5/9.
 */
@Configuration
@ConditionalOnClass(SpringTemplateEngine.class)
public class ThymeleafConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ThymeleafTemplateRender thymeleafTemplateRender() {
        return new ThymeleafTemplateRender();
    }
}
