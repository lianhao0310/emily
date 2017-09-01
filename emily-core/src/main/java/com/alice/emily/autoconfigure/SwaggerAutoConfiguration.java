package com.alice.emily.autoconfigure;

import com.alice.emily.module.swagger.SwaggerProperties;
import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.TimeUnit;

/**
 * Created by lianhao on 2017/2/13.
 */
@Configuration
@ConditionalOnClass({ Docket.class })
@ConditionalOnProperty(prefix = "emily.swagger", value = "enabled", havingValue = "true")
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration extends WebMvcConfigurerAdapter {

    private final SwaggerProperties properties;

    public SwaggerAutoConfiguration(SwaggerProperties properties) {
        this.properties = properties;
    }


    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket swaggerSpringPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title(properties.getTitle())
                        .version(properties.getVersion())
                        .description(properties.getDescription())
                        .license(properties.getLicense())
                        .licenseUrl(properties.getLicenseUrl())
                        .build());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
    }
}
