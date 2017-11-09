package com.alice.emily.data.mongodb.support;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.ClassUtils;

/**
 * Created by lianhao on 2017/7/27.
 */
@UtilityClass
public class MongoEntityInformationSupport {

    @SuppressWarnings("unchecked")
    public static <T, ID> MongoEntityInformation<T, ID> getEntityInformation(MongoConverter converter, RepositoryMetadata metadata) {
        Class<T> domainClass = (Class<T>) metadata.getDomainType();
        MongoPersistentEntity<?> entity = converter.getMappingContext().getRequiredPersistentEntity(domainClass);
        MappingMongoEntityInformation<T, ID> entityInformation =
                new MappingMongoEntityInformation<>((MongoPersistentEntity<T>) entity, (Class<ID>) metadata.getIdType());
        return ClassUtils.isAssignable(Persistable.class, entity.getType())
                ? new PersistableMongoEntityInformation<>(entityInformation) : entityInformation;
    }

}
