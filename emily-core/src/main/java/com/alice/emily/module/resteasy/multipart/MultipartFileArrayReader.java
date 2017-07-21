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
import java.util.List;

/**
 * Created by lianhao on 2017/3/29.
 */
@Provider
@Consumes("multipart/*")
public class MultipartFileArrayReader extends AbstractMessageBodyReader<MultipartFile[]> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.isAssignableFrom(MultipartFile[].class);
    }

    @Override
    public MultipartFile[] readFrom(Class<MultipartFile[]> type, Type genericType, Annotation[] annotations,
                                    MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                                    InputStream entityStream) throws IOException, WebApplicationException {

        MultiValueMap<String, MultipartFile> multiFileMap = getMultiFileMap();
        if (multiFileMap == null) {
            return new MultipartFile[0];
        }
        String name = getName(annotations);
        List<MultipartFile> files = multiFileMap.get(name);
        return files == null ? new MultipartFile[0] : files.toArray(new MultipartFile[files.size()]);
    }
}
