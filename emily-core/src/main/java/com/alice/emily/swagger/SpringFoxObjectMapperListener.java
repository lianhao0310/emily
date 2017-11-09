package com.alice.emily.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alice.emily.jackson.JacksonObjectMapperConfigurer;
import com.alice.emily.jackson.msgpack.MessagePackMapper;
import com.alice.emily.utils.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import springfox.documentation.schema.configuration.ObjectMapperConfigured;

import javax.annotation.Nonnull;

/**
 * Created by lianhao on 2017/7/28.
 */
public class SpringFoxObjectMapperListener implements ApplicationListener<ObjectMapperConfigured> {

    @Autowired
    private JacksonObjectMapperConfigurer configurer;

    @Override
    public void onApplicationEvent(@Nonnull ObjectMapperConfigured event) {
        ObjectMapper objectMapper = event.getObjectMapper();
        if (objectMapper != null
                && JSON.getObjectMapper() != objectMapper
                && !MessagePackMapper.class.isAssignableFrom(objectMapper.getClass())) {
            configurer.configure(objectMapper);
        }
    }

}
