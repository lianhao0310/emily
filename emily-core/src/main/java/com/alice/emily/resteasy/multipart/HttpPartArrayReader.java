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
import java.util.Collection;

/**
 * Created by lianhao on 2017/3/29.
 */
@Provider
@Consumes("multipart/*")
public class HttpPartArrayReader extends AbstractMultipartReader<Part[]> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.isAssignableFrom(Part[].class);
    }

    @Override
    public Part[] readFrom(Class<Part[]> type, Type genericType, Annotation[] annotations,
                           MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                           InputStream entityStream) throws IOException, WebApplicationException {
        String name = getName(annotations);
        try {
            Collection<Part> parts = request.getParts();
            return parts.stream().filter(part -> part.getName().equals(name)).toArray(Part[]::new);
        } catch (ServletException e) {
            throw new BadRequestException("Get parts from request failed: ", e);
        }
    }
}
