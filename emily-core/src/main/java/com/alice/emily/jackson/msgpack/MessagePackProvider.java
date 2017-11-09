package com.alice.emily.jackson.msgpack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.alice.emily.utils.HTTP;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Created by lianhao on 2017/3/20.
 */
@Provider
@Produces(HTTP.APPLICATION_X_MSGPACK)
@Consumes(HTTP.APPLICATION_X_MSGPACK)
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
