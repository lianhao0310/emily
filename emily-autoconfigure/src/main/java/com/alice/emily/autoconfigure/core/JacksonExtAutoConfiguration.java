package com.alice.emily.autoconfigure.core;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alice.emily.jackson.JacksonObjectMapperConfigurer;
import com.alice.emily.jackson.JacksonObjectMapperConfigurerInitializer;
import com.alice.emily.jackson.msgpack.MessagePackConfiguration;
import com.alice.emily.jackson.msgpack.MessagePackMapper;
import com.alice.emily.utils.JSON;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * Created by lianhao on 2017/3/17.
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@Import({ MessagePackConfiguration.class })
@AutoConfigureAfter(JacksonAutoConfiguration.class)
@EnableConfigurationProperties(JacksonProperties.class)
public class JacksonExtAutoConfiguration {

    private final JacksonProperties properties;

    public JacksonExtAutoConfiguration(JacksonProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public JacksonObjectMapperConfigurer jacksonObjectMapperConfigurer(ObjectProvider<List<Module>> modules) {
        return new JacksonObjectMapperConfigurer(properties, modules.getIfAvailable());
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(value = ObjectMapper.class, ignored = MessagePackMapper.class)
    public ObjectMapper objectMapper() {
        return JSON.getObjectMapper();
    }

    @Bean
    public JacksonObjectMapperConfigurerInitializer jacksonObjectMapperConfigurerInitializer() {
        return new JacksonObjectMapperConfigurerInitializer();
    }

}
