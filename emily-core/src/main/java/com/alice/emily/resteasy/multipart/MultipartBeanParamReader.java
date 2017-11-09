package com.alice.emily.resteasy.multipart;

import com.google.common.base.Strings;
import com.alice.emily.core.SpringContext;
import com.alice.emily.utils.Enums;
import lombok.Cleanup;
import org.jboss.resteasy.plugins.providers.ProviderHelper;
import org.jboss.resteasy.util.FindAnnotation;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by lianhao on 2017/3/29.
 */
@Provider
@Consumes({ MediaType.MULTIPART_FORM_DATA })
public class MultipartBeanParamReader extends AbstractMultipartReader<Object> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return FindAnnotation.findAnnotation(annotations, MultipartBeanParam.class) != null
                || type.isAnnotationPresent(MultipartBeanParam.class);
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {

        Object obj = BeanUtils.instantiateClass(type);
        Class<?> theType = type;
        while (theType != null && !theType.equals(Object.class)) {
            readValueAndWriteFields(theType, obj);
            theType = theType.getSuperclass();
        }
        readValueAndInvokeSetter(type, obj);
        return obj;
    }

    private void readValueAndWriteFields(Class<?> type, Object obj) throws WebApplicationException {
        try {
            for (Field field : type.getDeclaredFields()) {
                if (FindAnnotation.findAnnotation(field.getAnnotations(), FormParam.class) != null
                        || FindAnnotation.findAnnotation(field.getAnnotations(), MultipartParam.class) != null) {
                    Class<?> fieldType = field.getType();
                    Type genericType = field.getGenericType();
                    Annotation[] annotations = field.getAnnotations();
                    String name = getName(annotations);
                    name = StringUtils.isEmpty(name) ? field.getName() : name;
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

    private void readValueAndInvokeSetter(Class<?> type, Object obj) {
        try {
            for (Method method : type.getMethods()) {
                if (method.getName().startsWith("set")
                        && method.getParameterTypes().length == 1
                        && ((AnnotationUtils.findAnnotation(method, FormParam.class) != null
                        || AnnotationUtils.findAnnotation(method, MultipartParam.class) != null))) {
                    Class<?> parameterType = method.getParameterTypes()[0];
                    Type genericType = method.getGenericParameterTypes()[0];
                    Annotation[] annotations = method.getAnnotations();
                    String name = getName(annotations);
                    if (StringUtils.isEmpty(name)) {
                        name = StringUtils.uncapitalize(method.getName().substring(3));
                    }
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

        MultivaluedMap<String, String> headers = getHeaders(part);
        MediaType mediaType = Strings.isNullOrEmpty(part.getContentType())
                ? MediaType.APPLICATION_JSON_TYPE
                : MediaType.valueOf(part.getContentType());
        @Cleanup InputStream inputStream = part.getInputStream();

        // try enum
        if (type.isEnum()) {
            String value = ProviderHelper.readString(inputStream, mediaType);
            return Enums.fromStringFuzzy(value, ((Class<? extends Enum>) type).getEnumConstants());
        }

        // try primitive
        if (ClassUtils.isPrimitiveOrWrapper(type) || ClassUtils.isPrimitiveArray(type)) {
            String value = ProviderHelper.readString(inputStream, mediaType);
            return SpringContext.conversionService().convert(value, type);
        }

        MessageBodyReader<?> reader;

        // try multipart
        if (Part.class.isAssignableFrom(type) || Part[].class.isAssignableFrom(type)
                || MultipartFile.class.isAssignableFrom(type)
                || MultipartFile[].class.isAssignableFrom(type)) {
            reader = workers.getMessageBodyReader(type, genericType, annotations, MediaType.MULTIPART_FORM_DATA_TYPE);
            if (reader != null) {
                return reader.readFrom((Class) type, genericType, annotations,
                        MediaType.MULTIPART_FORM_DATA_TYPE, headers, inputStream);
            }
        }

        // try origin type
        reader = workers.getMessageBodyReader(type, genericType, annotations, mediaType);
        if (reader == null) {
            throw new WebApplicationException("Unable to find a MessageBodyReader for media type: "
                    + mediaType + " and class type " + type.getName());
        }

        return reader.readFrom((Class) type, genericType, annotations, mediaType, headers, inputStream);
    }
}
