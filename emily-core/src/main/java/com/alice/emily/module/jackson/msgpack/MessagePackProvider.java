package com.alice.emily.module.jackson.msgpack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Created by lianhao on 2017/3/20.
 */
@Provider
@Produces("application/x-msgpack")
@Consumes("application/x-msgpack")
public class MessagePackProvider extends JacksonJsonProvider {

    private static final String X_MSGPACK = "x-msgpack";

    public MessagePackProvider(ObjectMapper objectMapper) {
        setMapper(objectMapper);
    }

    @Override
    protected boolean hasMatchingMediaType(MediaType mediaType) {
        if (mediaType != null) {
            String subType = mediaType.getSubtype();
            return X_MSGPACK.equals(subType);
        }
        return false;
    }
}
