package com.alice.emily.template.beetl;

import lombok.extern.log4j.Log4j2;
import org.beetl.core.GroupTemplate;
import org.beetl.core.parser.BeetlParser;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by lianhao on 2017/5/3.
 */
@Log4j2
@Configuration
@ConditionalOnClass({ BeetlParser.class, GroupTemplate.class })
@EnableConfigurationProperties(BeetlProperties.class)
public class BeetlConfiguration {

    private final ApplicationContext applicationContext;
    private final BeetlProperties properties;

    public BeetlConfiguration(ApplicationContext applicationContext, BeetlProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    @PostConstruct
    public void checkTemplateLocationExists() {
        if (this.properties.isCheckTemplateLocation()) {
            TemplateLocation templatePathLocation = null;
            TemplateLocation location =
                    new TemplateLocation(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + properties.getRoot());
            if (location.exists(this.applicationContext)) {
                templatePathLocation = location;
            }
            if (templatePathLocation == null) {
                log.warn("Cannot find template location: " + location
                        + " (please add some templates, "
                        + "check your Beetl configuration, or set "
                        + "spring.beetl.checkTemplateLocation=false)");
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "spring.beetl")
    public org.beetl.core.Configuration beetlConfig() throws IOException {
        Properties properties = PropertiesLoaderUtils.loadProperties(this.properties.getConfig());
        return new org.beetl.core.Configuration(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public GroupTemplate beetlGroupTemplate(org.beetl.core.Configuration configuration) throws IOException {
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader(properties.getRoot());
        return new GroupTemplate(resourceLoader, configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public BeetlTemplateRender beetlTemplateRender(GroupTemplate template) {
        return new BeetlTemplateRender(template, properties);
    }

    @Configuration
    @ConditionalOnWebApplication
    public static class BeetlWebConfiguration {

        @Lazy
        @Bean(initMethod = "init")
        @ConditionalOnMissingBean(org.beetl.ext.spring.BeetlGroupUtilConfiguration.class)
        public BeetlGroupUtilConfiguration beetlGroupUtilConfiguration(GroupTemplate groupTemplate) {
            return new BeetlGroupUtilConfiguration(groupTemplate);
        }

        @Bean
        public BeetlSpringViewResolver beetlSpringViewResolver(BeetlProperties properties,
                                                               @Lazy org.beetl.ext.spring.BeetlGroupUtilConfiguration configuration) {
            BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
            properties.applyToViewResolver(beetlSpringViewResolver);
            beetlSpringViewResolver.setConfig(configuration);
            return beetlSpringViewResolver;
        }
    }
}
