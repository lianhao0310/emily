package com.alice.emily.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by liupin on 2017/2/13.
 */
@Configuration
@ConditionalOnClass({ Docket.class })
@ConditionalOnProperty(prefix = "emily.swagger", value = "enabled", havingValue = "true")
@EnableSwagger2
public class SwaggerAutoConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket swaggerSpringPlugin() {
        return new Docket(DocumentationType.SWAGGER_2);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
}
