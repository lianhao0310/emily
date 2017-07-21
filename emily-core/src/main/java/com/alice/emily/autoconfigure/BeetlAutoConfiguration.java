package com.alice.emily.autoconfigure;

import com.alice.emily.module.template.BeetlTemplateRender;
import com.alice.emily.module.template.TemplateRender;
import com.alice.emily.utils.Errors;
import org.beetl.core.GroupTemplate;
import org.beetl.core.parser.BeetlParser;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import javax.servlet.http.HttpServlet;
import java.io.IOException;

@Configuration
@ConditionalOnClass({ BeetlParser.class, GroupTemplate.class })
public class BeetlAutoConfiguration {

    static final String ROOT = "template/";

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnClass({ ServletContextAware.class, HttpServlet.class, AbstractTemplateViewResolver.class })
    static class BeetlWebConfiguration {

        @Bean(initMethod = "init", name = "beetlConfig")
        public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
            BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
            try {
                ClasspathResourceLoader cploder = new ClasspathResourceLoader(BeetlAutoConfiguration.class.getClassLoader(), ROOT);
                beetlGroupUtilConfiguration.setResourceLoader(cploder);
                return beetlGroupUtilConfiguration;
            } catch (Exception e) {
                throw Errors.rethrow(e, "Cannot create beetl configuration");
            }
        }

        @Bean(name = "beetlViewResolver")
        public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration configuration) {
            BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
            beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
            beetlSpringViewResolver.setOrder(0);
            beetlSpringViewResolver.setConfig(configuration);
            return beetlSpringViewResolver;
        }

        @Bean(name = "beetlGroupTemplate")
        public GroupTemplate getGroupTemplate(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration configuration) {
            return configuration.getGroupTemplate();
        }
    }

    @Configuration
    @AutoConfigureAfter(BeetlWebConfiguration.class)
    static class BeetlConfiguration {
        @Bean(name = "beetlGroupTemplate")
        @ConditionalOnMissingBean(GroupTemplate.class)
        public GroupTemplate getGroupTemplate() throws IOException {
            ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("template/");
            org.beetl.core.Configuration cfg = org.beetl.core.Configuration.defaultConfiguration();
            return new GroupTemplate(resourceLoader, cfg);
        }

        @Bean
        @ConditionalOnMissingBean({ TemplateRender.class })
        public TemplateRender getBeetlTemplateRender(GroupTemplate template) {
            return new BeetlTemplateRender(template);
        }
    }
}