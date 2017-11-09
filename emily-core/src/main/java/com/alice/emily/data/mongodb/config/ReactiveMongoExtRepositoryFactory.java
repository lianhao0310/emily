package com.alice.emily.data.mongodb.config;

import com.alice.emily.data.mongodb.repository.ReactiveMongoOperationExecutor;
import com.alice.emily.data.mongodb.repository.SimpleReactiveMongoOperationExecutor;
import com.alice.emily.data.mongodb.support.MongoEntityInformationSupport;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFragment;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Created by lianhao on 2017/6/8.
 */
public class ReactiveMongoExtRepositoryFactory extends ReactiveMongoRepositoryFactory {

    private final ReactiveMongoOperations operations;

    /**
     * Creates a new {@link ReactiveMongoRepositoryFactory} with the given {@link ReactiveMongoOperations}.
     *
     * @param mongoOperations must not be {@literal null}.
     */
    public ReactiveMongoExtRepositoryFactory(ReactiveMongoOperations mongoOperations) {
        super(mongoOperations);
        this.operations = mongoOperations;
    }

    @Nonnull
    @Override
    protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
        RepositoryComposition.RepositoryFragments fragments = super.getRepositoryFragments(metadata);

        if (ReactiveMongoOperationExecutor.class.isAssignableFrom(metadata.getRepositoryInterface())) {
            MongoEntityInformation<?, Serializable> entityInformation =
                    MongoEntityInformationSupport.getEntityInformation(operations.getConverter(), metadata);
            Object repository = getTargetRepositoryViaReflection(
                    SimpleReactiveMongoOperationExecutor.class, entityInformation, operations);
            fragments = fragments.append(RepositoryFragment.implemented(repository));
        }

        return fragments;
    }
}
