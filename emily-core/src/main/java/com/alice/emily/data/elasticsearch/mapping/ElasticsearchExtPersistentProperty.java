package com.alice.emily.data.elasticsearch.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchPersistentProperty;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.Lazy;

import java.util.Objects;

/**
 * Created by lianhao on 2017/5/23.
 */
public class ElasticsearchExtPersistentProperty extends SimpleElasticsearchPersistentProperty {

    private final Lazy<Boolean> isId = Lazy.of(() -> isAnnotationPresent(Id.class));
    private final Lazy<String> fieldName = Lazy.of(this::getMoreSpecificFieldName);

    public ElasticsearchExtPersistentProperty(Property property,
                                              PersistentEntity<?, ElasticsearchPersistentProperty> owner,
                                              SimpleTypeHolder simpleTypeHolder) {
        super(property, owner, simpleTypeHolder);
    }

    @Override
    public boolean isIdProperty() {
        return isId.get();
    }

    @Override
    public String getFieldName() {
        return fieldName.get();
    }

    private String getMoreSpecificFieldName() {
        JsonProperty jsonProperty = findAnnotation(JsonProperty.class);
        if (jsonProperty != null && !Objects.equals(jsonProperty.value(), JsonProperty.USE_DEFAULT_NAME)) {
            return jsonProperty.value();
        }
        return super.getFieldName();
    }
}
