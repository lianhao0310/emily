package com.alice.emily.resteasy.multipart;

import com.alice.emily.resteasy.AbstractMessageBodyReader;
import org.jboss.resteasy.util.CaseInsensitiveMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MultivaluedMap;
import java.lang.annotation.Annotation;

/**
 * Created by lianhao on 2017/3/29.
 */
public abstract class AbstractMultipartReader<T> extends AbstractMessageBodyReader<T> {

    protected String getName(Annotation[] annotations) {
        String name = null;
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof MultipartParam) {
                    MultipartParam param = (MultipartParam) annotation;
                    name = param.value();
                    break;
                }
                if (annotation instanceof FormParam) {
                    FormParam param = (FormParam) annotation;
                    name = param.value();
                    break;
                }
            }
        }
        return name;
    }

    protected MultiValueMap<String, MultipartFile> getMultiFileMap() {
        return MultipartServletResolver.getMultiFileMap(request);
    }

    protected MultivaluedMap<String, String> getHeaders(Part part) {
        MultivaluedMap<String, String> headers = new CaseInsensitiveMap<>();
        for (String header : part.getHeaderNames()) {
            for (String value : part.getHeaders(header)) {
                headers.putSingle(header, value);
            }
        }
        return headers;
    }
}
