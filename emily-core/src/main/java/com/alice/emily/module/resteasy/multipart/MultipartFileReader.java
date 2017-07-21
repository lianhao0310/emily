package com.alice.emily.module.resteasy.multipart;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

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
 * Created by lianhao on 2017/3/29.
 */
@Provider
@Consumes("multipart/*")
public class MultipartFileReader extends AbstractMessageBodyReader<MultipartFile> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return MultipartFile.class.isAssignableFrom(type);
    }

    @Override
    public MultipartFile readFrom(Class<MultipartFile> type, Type genericType, Annotation[] annotations,
                                  MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                                  InputStream entityStream) throws IOException, WebApplicationException {

        MultiValueMap<String, MultipartFile> multiFileMap = getMultiFileMap();
        String name = getName(annotations);
        return multiFileMap == null ? null : multiFileMap.getFirst(name);
    }

}
