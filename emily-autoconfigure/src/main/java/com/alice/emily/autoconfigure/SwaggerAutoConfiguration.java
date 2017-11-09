package com.alice.emily.autoconfigure;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.alice.emily.core.SpringContext;
import com.alice.emily.swagger.SpringFoxExtPluginConfiguration;
import com.alice.emily.swagger.SpringFoxObjectMapperListener;
import com.alice.emily.swagger.SwaggerProperties;
import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * Created by lianhao on 2017/2/13.
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Docket.class })
@ConditionalOnProperty(prefix = "emily.swagger", name = "enabled", matchIfMissing = true)
@Import({ BeanValidatorPluginsConfiguration.class, SpringFoxExtPluginConfiguration.class })
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration implements WebMvcConfigurer {

    private final SwaggerProperties properties;

    public SwaggerAutoConfiguration(SwaggerProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket springFoxDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(SpringContext.basePackages()
                        .stream()
                        .map(RequestHandlerSelectors::basePackage)
                        .reduce(Predicates::or)
                        .orElse(RequestHandlerSelectors.any()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title(properties.getTitle())
                        .version(properties.getVersion())
                        .description(properties.getDescription())
                        .license(properties.getLicense())
                        .licenseUrl(properties.getLicenseUrl())
                        .build())
                .host(properties.determineHost())
                .pathMapping(properties.determinePath());
    }

    @Autowired(required = false)
    void configureDocket(Docket docket) {
        TypeResolver resolver = new TypeResolver();

        AlternateTypeRule deferredResultRule = AlternateTypeRules.newRule(
                resolver.resolve(DeferredResult.class, resolver.resolve(ResponseEntity.class, WildcardType.class)),
                resolver.resolve(WildcardType.class));

        docket.forCodeGeneration(true)
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(Geometry.class, GeoJson.class)
                .genericModelSubstitutes(ResponseEntity.class, DeferredResult.class)
                .genericModelSubstitutes(Future.class)
                .alternateTypeRules(deferredResultRule);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringFoxObjectMapperListener springFoxObjectMapperListener() {
        return new SpringFoxObjectMapperListener();
    }

    @Data
    private static class GeoJson<T extends Iterable<?>> {
        private String type;
        private T coordinates;
    }
}
