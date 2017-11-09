package com.alice.emily.swagger.converter;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Created by lianhao on 2017/5/11.
 */
public class PageableModelConverter extends AbstractModelConverter {

//    @Override
//    public Property resolveProperty(Type type, ModelConverterContext context,
//                                    Annotation[] annotations, Iterator<ModelConverter> chain) {
//        JavaType javaType = Json.mapper().constructType(type);
//
//        if (javaType != null) {
//            Class<?> rawClass = javaType.getRawClass();
//
//            if (Pageable.class.isAssignableFrom(rawClass)) {
//                ObjectProperty property = new ObjectProperty();
//                property.property("page", new IntegerProperty().description("Page you want to retrieve."));
//                property.property("size", new IntegerProperty().description("Size of the page you want to retrieve."));
//                property.property("sort", new StringProperty().description("Properties that should be sorted by in " +
//                        "the format property,property(,ASC|DESC). Default sort direction is ascending. Use multiple " +
//                        "sort parameters if you want to switch directions, e.g. ?sort=firstname&sort=lastname,asc."));
//                return property;
//            }
//
//            return super.resolveProperty(type, context, annotations, chain);
//        }
//        return null;
//    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {

        JavaType javaType = Json.mapper().constructType(type);

        if (javaType != null) {
            Class<?> rawClass = javaType.getRawClass();
            if (Pageable.class.isAssignableFrom(rawClass)) {
                ModelImpl model = new ModelImpl();
                model.setName(rawClass.getSimpleName());
                model.setDescription("Pageable is always present as query param for a http request, Please note that!");
                model.addProperty("page", new IntegerProperty().description("Page you want to retrieve."));
                model.addProperty("size", new IntegerProperty().description("Size of the page you want to retrieve."));
                model.addProperty("sort", new StringProperty().description("Properties that should be sorted by in " +
                        "the format property,property(,ASC|DESC). Default sort direction is ascending. Use multiple " +
                        "sort parameters if you want to switch directions, e.g. ?sort=firstname&sort=lastname,asc."));
                context.defineModel(model.getName(), model);
                return model;
            } else {
                return super.resolve(type, context, chain);
            }
        }

        return null;
    }

}