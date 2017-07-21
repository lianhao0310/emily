package com.alice.emily.module.jackson;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.hibernate.Hibernate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lianhao on 2017/3/23.
 */
@Configuration
public class JacksonModulesConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Jdk8Module jdk8Module() {
        return new Jdk8Module();
    }

    @Bean
    @ConditionalOnMissingBean
    public GuavaModule guavaModule() {
        return new GuavaModule();
    }

    @Bean
    @ConditionalOnMissingBean
    public GuavaExtrasModule guavaExtrasModule() {
        return new GuavaExtrasModule();
    }

    @Bean
    @ConditionalOnMissingBean
    public FuzzyEnumModule fuzzyEnumModule() {
        return new FuzzyEnumModule();
    }

    @Bean
    @ConditionalOnMissingBean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Configuration
    @ConditionalOnClass({ JtsModule.class, GeometryFactory.class })
    static class JacksonJtsModuleProvider {

        @Bean
        @ConditionalOnMissingBean
        public JtsModule jtsModule() {
            return new JtsModule();
        }

    }

    @Configuration
    @ConditionalOnClass({ Hibernate5Module.class, Hibernate.class })
    static class Hibernate5ModuleProvider {

        @Bean
        @ConditionalOnMissingBean
        public Hibernate5Module hibernate5Module() {
            return new Hibernate5Module();
        }
    }
}
