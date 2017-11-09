package com.alice.emily.data.mongodb.support;

import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentProperty;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;

/**
 * Created by lianhao on 2017/8/4.
 */
public class MongoExtPersistentProperty extends BasicMongoPersistentProperty {

    private Boolean isIdProperty;
    private Boolean isAssociation;
    private String fieldName;
    private Boolean usePropertyAccess;
    private Boolean isTransient;

    /**
     * Creates a new {@link MongoExtPersistentProperty}.
     *
     * @param property
     * @param owner
     * @param simpleTypeHolder
     * @param fieldNamingStrategy
     */
    public MongoExtPersistentProperty(Property property,
                                      MongoPersistentEntity<?> owner,
                                      SimpleTypeHolder simpleTypeHolder, FieldNamingStrategy fieldNamingStrategy) {
        super(property, owner, simpleTypeHolder, fieldNamingStrategy);
    }

    @Override
    public boolean isIdProperty() {
        if (this.isIdProperty == null) {
            this.isIdProperty = isExplicitIdProperty();
        }
        return this.isIdProperty;
    }

    @Override
    public boolean isAssociation() {
        if (this.isAssociation == null) {
            this.isAssociation = super.isAssociation();
        }
        return this.isAssociation;
    }

    @Override
    public String getFieldName() {
        if (this.fieldName == null) {
            this.fieldName = super.getFieldName();
        }
        return this.fieldName;
    }

    @Override
    public boolean usePropertyAccess() {
        if (this.usePropertyAccess == null) {
            this.usePropertyAccess = super.usePropertyAccess();
        }
        return this.usePropertyAccess;
    }

    @Override
    public boolean isTransient() {
        if (this.isTransient == null) {
            this.isTransient = super.isTransient();
        }
        return this.isTransient;
    }

}
