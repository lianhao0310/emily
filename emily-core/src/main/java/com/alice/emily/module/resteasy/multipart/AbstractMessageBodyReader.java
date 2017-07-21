package com.alice.emily.module.resteasy.multipart;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.MessageBodyReader;
import java.lang.annotation.Annotation;

/**
 * Created by lianhao on 2017/3/29.
 */
public abstract class AbstractMessageBodyReader<T> implements MessageBodyReader<T> {

    @Context
    protected HttpServletRequest request;

    protected String getName(Annotation[] annotations) {
        String filename = null;
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof MultipartFileParam) {
                    MultipartFileParam param = (MultipartFileParam) annotation;
                    filename = param.value();
                    break;
                }
                if (annotation instanceof FormParam) {
                    FormParam param = (FormParam) annotation;
                    filename = param.value();
                    break;
                }
            }
        }
        return filename;
    }

    protected MultiValueMap<String, MultipartFile> getMultiFileMap() {
        return MultipartServletResolver.getMultiFileMap(request);
    }

}
