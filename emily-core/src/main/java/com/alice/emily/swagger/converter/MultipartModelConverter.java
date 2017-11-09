package com.alice.emily.swagger.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.alice.emily.resteasy.multipart.MultipartParam;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import javax.ws.rs.FormParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Created by lianhao on 2017/5/11.
 */
public class MultipartModelConverter extends AbstractModelConverter {

    @Override
    public Property resolveProperty(Type type, ModelConverterContext context,
                                    Annotation[] annotations, Iterator<ModelConverter> chain) {

        JavaType javaType = Json.mapper().constructType(type);

        if (javaType != null) {

            Property property = tryResolveSpecificProperty(javaType,
                    new FileProperty(), MultipartFile.class, Part.class);

            if (property != null) {

                FormParam formParam = findAnnotation(annotations, FormParam.class);
                if (formParam != null) {
                    property.setName(formParam.value());
                }

                MultipartParam multipartParam = findAnnotation(annotations, MultipartParam.class);
                if (multipartParam != null) {
                    property.setName(multipartParam.value());
                }

                return property;
            }

            return super.resolveProperty(type, context, annotations, chain);
        }

        return null;
    }

}