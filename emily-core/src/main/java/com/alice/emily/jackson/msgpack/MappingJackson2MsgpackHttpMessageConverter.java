package com.alice.emily.jackson.msgpack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alice.emily.utils.HTTP;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

/**
 * Created by lianhao on 2017/3/27.
 */
public class MappingJackson2MsgpackHttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    public MappingJackson2MsgpackHttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.valueOf(HTTP.APPLICATION_X_MSGPACK));
    }
}
