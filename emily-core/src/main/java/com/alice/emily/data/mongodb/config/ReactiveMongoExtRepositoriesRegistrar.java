package com.alice.emily.data.mongodb.config;

import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.mongodb.repository.config.ReactiveMongoRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * Created by lianhao on 2017/5/15.
 */
public class ReactiveMongoExtRepositoriesRegistrar extends AbstractRepositoryConfigurationSourceSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableReactiveMongoRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableReactiveMongoExtRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new ReactiveMongoRepositoryConfigurationExtension() {
            @Override
            public String getRepositoryFactoryBeanClassName() {
                return ReactiveMongoExtRepositoryFactoryBean.class.getName();
            }
        };
    }

    @EnableReactiveMongoRepositories(repositoryFactoryBeanClass = ReactiveMongoExtRepositoryFactoryBean.class)
    private static class EnableReactiveMongoExtRepositoriesConfiguration {

    }
}
