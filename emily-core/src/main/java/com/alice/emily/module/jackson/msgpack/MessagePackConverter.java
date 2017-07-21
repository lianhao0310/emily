package com.alice.emily.module.jackson.msgpack;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

/**
 * Created by lianhao on 2017/3/27.
 */
public class MessagePackConverter extends AbstractJackson2HttpMessageConverter {

    private static final MediaType APPLICATION_X_MSGPACK = MediaType.valueOf("application/x-msgpack");

    public MessagePackConverter(ObjectMapper objectMapper) {
        super(objectMapper, APPLICATION_X_MSGPACK);
    }
}
