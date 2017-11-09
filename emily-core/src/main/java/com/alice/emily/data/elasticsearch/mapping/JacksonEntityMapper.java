package com.alice.emily.data.elasticsearch.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;

/**
 * Created by lianhao on 2017/5/19.
 */
public class JacksonEntityMapper implements EntityMapper {

    private final ObjectMapper objectMapper;

    public JacksonEntityMapper(@NonNull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String mapToString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
        return objectMapper.readValue(source, clazz);
    }
}
