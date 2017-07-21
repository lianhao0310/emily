package com.alice.emily.autoconfigure;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.alice.emily.module.jackson.AnnotationSensitivePropertyNamingStrategy;
import com.alice.emily.module.jackson.JacksonModulesConfiguration;
import com.alice.emily.module.jackson.JacksonObjectMapperConfigurer;
import com.alice.emily.module.jackson.msgpack.MessagePackConfiguration;
import com.alice.emily.utils.JSONUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Collection;
import java.util.List;

/**
 * Created by lianhao on 2017/3/17.
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@Import({ JacksonModulesConfiguration.class, MessagePackConfiguration.class })
@AutoConfigureAfter(JacksonAutoConfiguration.class)
@EnableConfigurationProperties(JacksonProperties.class)
public class JacksonExtAutoConfiguration {

    private final JacksonProperties properties;
    private final ObjectProvider<List<Module>> modules;

    public JacksonExtAutoConfiguration(JacksonProperties properties, ObjectProvider<List<Module>> modules) {
        this.properties = properties;
        this.modules = modules;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public JacksonObjectMapperConfigurer jacksonObjectMapperConfigurer() {
        return new JacksonObjectMapperConfigurer(properties, modules.getIfAvailable());
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(value = ObjectMapper.class,
            ignoredType = "com.alice.emily.module.jackson.msgpack.MessagePackMapper")
    public ObjectMapper objectMapper() {
        return JSONUtils.getMapper();
    }

    @Autowired
    public void configureObjectMappers(ObjectMapper objectMapper,
                                       ObjectProvider<Collection<ObjectMapper>> objectMappersProvider,
                                       ObjectProvider<Collection<JacksonObjectMapperConfigurer>> configurers) {
        // set primary object mapper to JSONUtils
        JSONUtils.setMapper(objectMapper);
        Collection<ObjectMapper> objectMappers = objectMappersProvider.getIfAvailable();
        Collection<JacksonObjectMapperConfigurer> objectMapperConfigurers = configurers.getIfAvailable();

        if (objectMappers != null && !objectMappers.isEmpty()) {
            for (ObjectMapper mapper : objectMappers) {

                // use provided JacksonObjectMapperConfigurer configure the object mapper
                if (objectMapperConfigurers != null && !objectMapperConfigurers.isEmpty()) {
                    for (JacksonObjectMapperConfigurer objectMapperConfigurer : objectMapperConfigurers) {
                        objectMapperConfigurer.configure(mapper);
                    }
                }

                // check property naming strategy, try use AnnotationSensitivePropertyNamingStrategy instead
                PropertyNamingStrategy strategy = mapper.getPropertyNamingStrategy();
                if (strategy == null || strategy.getClass().isAssignableFrom(PropertyNamingStrategy.class)) {
                    mapper.setPropertyNamingStrategy(new AnnotationSensitivePropertyNamingStrategy());
                }
            }
        }
    }
}
