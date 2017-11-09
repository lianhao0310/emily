package com.alice.emily.data.elasticsearch.mapping;

import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchPersistentEntity;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;

/**
 * Created by lianhao on 2017/5/23.
 */
public class ElasticsearchMappingContext extends SimpleElasticsearchMappingContext {

    @Override
    protected ElasticsearchPersistentProperty createPersistentProperty(Property property,
                                                                       SimpleElasticsearchPersistentEntity<?> owner,
                                                                       SimpleTypeHolder simpleTypeHolder) {
        return new ElasticsearchExtPersistentProperty(property, owner, simpleTypeHolder);
    }
}
