package com.alice.emily.module.resteasy.multipart;

import org.jboss.resteasy.util.Types;
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by liupin on 2017/3/29.
 */
@Provider
@Consumes("multipart/*")
public class MultipartFileListReader extends AbstractMessageBodyReader<List<MultipartFile>> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        boolean isReadable = type.equals(List.class) && genericType != null && genericType instanceof ParameterizedType;
        if (isReadable) {
            ParameterizedType param = (ParameterizedType) genericType;
            Type baseType = param.getActualTypeArguments()[0];
            Class<?> rawType = Types.getRawType(baseType);
            isReadable = MultipartFile.class.isAssignableFrom(rawType);
        }
        return isReadable;
    }

    @Override
    public List<MultipartFile> readFrom(Class<List<MultipartFile>> type, Type genericType, Annotation[] annotations,
                                        MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                                        InputStream entityStream) throws IOException, WebApplicationException {
        MultiValueMap<String, MultipartFile> multiFileMap = getMultiFileMap();
        if (multiFileMap == null) return Collections.emptyList();
        String name = getName(annotations);
        List<MultipartFile> multipartFiles = multiFileMap.get(name);
        return multipartFiles == null ? Collections.emptyList() : multipartFiles;
    }
}
