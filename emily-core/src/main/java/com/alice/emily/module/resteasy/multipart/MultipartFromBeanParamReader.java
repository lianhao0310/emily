package com.alice.emily.module.resteasy.multipart;

import com.google.common.base.Strings;
import com.alice.emily.utils.Reflection;
import org.jboss.resteasy.util.CaseInsensitiveMap;
import org.jboss.resteasy.util.FindAnnotation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by liupin on 2017/3/29.
 */
@Provider
@Consumes("multipart/form-data")
public class MultipartFromBeanParamReader extends AbstractMessageBodyReader<Object> {

    @Context
    protected Providers workers;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return FindAnnotation.findAnnotation(annotations, MultipartBeanParam.class) != null
                || type.isAnnotationPresent(MultipartBeanParam.class);
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {

        Object obj = Reflection.newInstance(type);
        Class<?> theType = type;
        while (theType != null && !theType.equals(Object.class)) {
            readFields(theType, obj);
            theType = theType.getSuperclass();
        }
        readSetter(type, obj);
        return obj;
    }

    private void readFields(Class<?> type, Object obj) throws WebApplicationException {
        try {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(FormParam.class) || field.isAnnotationPresent(MultipartFileParam.class)) {
                    Class<?> fieldType = field.getType();
                    Type genericType = field.getGenericType();
                    Annotation[] annotations = field.getAnnotations();
                    String name = getName(annotations);
                    Object value = readValue(name, fieldType, genericType, annotations);
                    if (value != null) {
                        field.setAccessible(true);
                        field.set(obj, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new WebApplicationException("Unable to read fields for " + type.getSimpleName(), e);
        }
    }

    private void readSetter(Class<?> type, Object obj) {
        try {
            for (Method method : type.getMethods()) {
                if ((method.isAnnotationPresent(FormParam.class) || method.isAnnotationPresent(MultipartFileParam.class))
                        && method.getName().startsWith("set")
                        && method.getParameterTypes().length == 1) {
                    Class<?> parameterType = method.getParameterTypes()[0];
                    Type genericType = method.getGenericParameterTypes()[0];
                    Annotation[] annotations = method.getAnnotations();
                    String name = getName(annotations);
                    Object value = readValue(name, parameterType, genericType, annotations);
                    if (value != null) {
                        method.invoke(obj, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new WebApplicationException("Unable to read setter for " + type.getSimpleName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private Object readValue(String name, Class<?> type, Type genericType,
                             Annotation[] annotations) throws IOException, ServletException {
        Part part = request.getPart(name);
        if (part == null) {
            return null;
        }
        MultivaluedMap<String, String> headers = new CaseInsensitiveMap<>();
        for (String header : part.getHeaderNames()) {
            for (String value : part.getHeaders(header)) {
                headers.putSingle(header, value);
            }
        }
        String contentType = part.getContentType();
        if (MultipartFile.class.isAssignableFrom(type)) {
            contentType = MediaType.MULTIPART_FORM_DATA;
        }
        if (Strings.isNullOrEmpty(contentType)) {
            contentType = MediaType.MEDIA_TYPE_WILDCARD;
        }
        MediaType mediaType = MediaType.valueOf(contentType);
        MessageBodyReader<?> reader = workers.getMessageBodyReader(type, genericType, annotations, mediaType);
        if (reader == null) {
            throw new WebApplicationException("Unable to find a MessageBodyReader for media type: "
                    + mediaType + " and class type " + type.getName());
        }
        try (InputStream inputStream = part.getInputStream()) {
            return reader.readFrom((Class) type, genericType, annotations, mediaType, headers, inputStream);
        }
    }
}
