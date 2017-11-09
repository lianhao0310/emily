package com.alice.emily.data.mongodb.config;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

/**
 * Created by lianhao on 2017/6/8.
 */
public class ReactiveMongoExtRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends ReactiveMongoRepositoryFactoryBean<T, S, ID> {

    /**
     * Creates a new {@link ReactiveMongoRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public ReactiveMongoExtRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport getFactoryInstance(ReactiveMongoOperations operations) {
        return new ReactiveMongoExtRepositoryFactory(operations);
    }
}
