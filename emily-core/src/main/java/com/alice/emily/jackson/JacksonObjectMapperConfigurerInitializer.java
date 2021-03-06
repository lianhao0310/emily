package com.alice.emily.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.alice.emily.utils.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by lianhao on 2017/4/13.
 */
public class JacksonObjectMapperConfigurerInitializer implements InitializingBean {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private List<ObjectMapper> objectMappers;

    @Autowired(required = false)
    private List<JacksonObjectMapperConfigurer> configurers;

    @Override
    public void afterPropertiesSet() throws Exception {
        // set primary object mapper to JSON
        if (!Objects.equals(objectMapper, JSON.getObjectMapper())) {
            JSON.setObjectMapper(objectMapper);
        }
        if (!CollectionUtils.isEmpty(objectMappers)) {
            for (ObjectMapper mapper : objectMappers) {
                configure(mapper);
                setNamingStrategy(mapper);
            }
        }
    }

    private void configure(ObjectMapper mapper) {
        if (!CollectionUtils.isEmpty(configurers)) {
            for (JacksonObjectMapperConfigurer objectMapperConfigurer : configurers) {
                objectMapperConfigurer.configure(mapper);
            }
        }
    }

    // check property naming strategy, try use AnnotationSensitivePropertyNamingStrategy instead
    private void setNamingStrategy(ObjectMapper mapper) {
        PropertyNamingStrategy strategy = mapper.getPropertyNamingStrategy();
        if (strategy == null || strategy.getClass().isAssignableFrom(PropertyNamingStrategy.class)) {
            mapper.setPropertyNamingStrategy(new AnnotationSensitivePropertyNamingStrategy());
        }
    }

}
