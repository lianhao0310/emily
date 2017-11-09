package com.alice.emily.data.elasticsearch.config;

import lombok.Getter;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

/**
 * Created by lianhao on 2017/6/8.
 */
public class ElasticsearchExtRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends ElasticsearchRepositoryFactoryBean<T, S, ID> {

    @Getter
    private ElasticsearchOperations operations;

    /**
     * Creates a new {@link ElasticsearchRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public ElasticsearchExtRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    /**
     * Configures the {@link ElasticsearchOperations} to be used to create Elasticsearch repositories.
     *
     * @param operations the operations to set
     */
    @Override
    public void setElasticsearchOperations(ElasticsearchOperations operations) {
        super.setElasticsearchOperations(operations);
        this.operations = operations;
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        return new ElasticsearchExtRepositoryFactory(operations);
    }
}
