package com.alice.emily.data.mongodb.support;

import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

/**
 * Created by lianhao on 2017/8/4.
 */
public class MongoExtMappingContext extends MongoMappingContext {

    private FieldNamingStrategy fieldNamingStrategy = PropertyNameFieldNamingStrategy.INSTANCE;

    @Override
    public MongoPersistentProperty createPersistentProperty(Property property,
                                                            BasicMongoPersistentEntity<?> owner,
                                                            SimpleTypeHolder simpleTypeHolder) {
        return new MongoExtPersistentProperty(property, owner, simpleTypeHolder, fieldNamingStrategy);
    }

    @Override
    public void setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        if (fieldNamingStrategy != null) {
            super.setFieldNamingStrategy(fieldNamingStrategy);
            this.fieldNamingStrategy = fieldNamingStrategy;
        }
    }

}
