package com.alice.emily.swagger.converter;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.models.Model;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Created by lianhao on 2017/5/11.
 */
public abstract class AbstractModelConverter implements ModelConverter {

    @Override
    public Property resolveProperty(Type type, ModelConverterContext context,
                                    Annotation[] annotations, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            return chain.next().resolveProperty(type, context, annotations, chain);
        } else {
            return null;
        }
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends Annotation> T findAnnotation(Annotation[] annotations, Class<T> type) {
        if (!ArrayUtils.isEmpty(annotations)) {
            for (Annotation annotation : annotations) {
                if (type.isInstance(annotation)) {
                    return (T) annotation;
                }
            }
        }
        return null;
    }

    protected Property tryResolveSpecificProperty(JavaType type, Property property, Class<?>... classes) {
        if (ArrayUtils.isEmpty(classes)) {
            return null;
        }

        JavaType comparedType = type;
        if (type.isArrayType() || type.isCollectionLikeType()) {
            comparedType = type.getContentType();
        }

        boolean matched = false;
        for (Class<?> aClass : classes) {
            Class<?> rawClass = comparedType.getRawClass();
            if (aClass.isAssignableFrom(rawClass)) {
                matched = true;
                break;
            }
        }

        if (matched) {
            return type == comparedType ? property : new ArrayProperty(property);
        }

        return null;
    }
}