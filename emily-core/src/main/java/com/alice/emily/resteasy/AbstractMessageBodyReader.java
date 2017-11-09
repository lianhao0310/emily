package com.alice.emily.resteasy;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Providers;
import java.lang.annotation.Annotation;

/**
 * Created by lianhao on 2017/5/10.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractMessageBodyReader<T> implements MessageBodyReader<T> {

    @Context
    protected HttpServletRequest request;

    @Context
    protected Providers workers;

    /**
     * Returns the value of the given specific property of the given annotation. If the value of that property is the
     * properties default, we fall back to the value of the {@code value} attribute.
     *
     * @param annotation must not be {@literal null}.
     * @param property   must not be {@literal null} or empty.
     * @return T
     */
    protected static <T> T getSpecificPropertyOrDefaultFromValue(Annotation annotation, String property) {

        Object propertyDefaultValue = AnnotationUtils.getDefaultValue(annotation, property);
        Object propertyValue = AnnotationUtils.getValue(annotation, property);

        return (T) (ObjectUtils.nullSafeEquals(propertyDefaultValue, propertyValue) ? AnnotationUtils.getValue(annotation)
                : propertyValue);
    }
}
