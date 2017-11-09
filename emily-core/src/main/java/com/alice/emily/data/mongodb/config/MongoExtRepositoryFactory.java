package com.alice.emily.data.mongodb.config;

import com.alice.emily.data.mongodb.repository.MongoOperationExecutor;
import com.alice.emily.data.mongodb.repository.SimpleMongoOperationExecutor;
import com.alice.emily.data.mongodb.support.MongoEntityInformationSupport;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFragment;

import java.io.Serializable;

/**
 * Created by lianhao on 2017/6/8.
 */
public class MongoExtRepositoryFactory extends MongoRepositoryFactory {

    private final MongoOperations operations;

    /**
     * Creates a new {@link MongoRepositoryFactory} with the given {@link MongoOperations}.
     *
     * @param mongoOperations must not be {@literal null}.
     */
    public MongoExtRepositoryFactory(MongoOperations mongoOperations) {
        super(mongoOperations);
        this.operations = mongoOperations;
    }

    @Override
    protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
        RepositoryComposition.RepositoryFragments fragments = super.getRepositoryFragments(metadata);

        if (MongoOperationExecutor.class.isAssignableFrom(metadata.getRepositoryInterface())) {
            MongoEntityInformation<?, Serializable> entityInformation =
                    MongoEntityInformationSupport.getEntityInformation(operations.getConverter(), metadata);
            Object repository = getTargetRepositoryViaReflection(
                    SimpleMongoOperationExecutor.class, entityInformation, operations);
            fragments = fragments.append(RepositoryFragment.implemented(repository));
        }

        return fragments;
    }

}
