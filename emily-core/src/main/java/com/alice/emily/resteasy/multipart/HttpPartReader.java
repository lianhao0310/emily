package com.alice.emily.resteasy.multipart;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by lianhao on 2017/5/1.
 */
@Provider
@Consumes("multipart/*")
public class HttpPartReader extends AbstractMultipartReader<Part> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Part.class.isAssignableFrom(type);
    }

    @Override
    public Part readFrom(Class<Part> type, Type genericType, Annotation[] annotations,
                         MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                         InputStream entityStream) throws IOException, WebApplicationException {
        String name = getName(annotations);
        try {
            return request.getPart(name);
        } catch (ServletException e) {
            throw new BadRequestException("Get part from request failed: " + name, e);
        }
    }
}