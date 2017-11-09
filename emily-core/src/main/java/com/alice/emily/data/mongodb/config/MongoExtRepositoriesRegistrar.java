package com.alice.emily.data.mongodb.config;

import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.MongoRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * Created by lianhao on 2017/5/15.
 */
public class MongoExtRepositoriesRegistrar extends AbstractRepositoryConfigurationSourceSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableMongoRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableMongoExtRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new MongoRepositoryConfigurationExtension() {
            @Override
            public String getRepositoryFactoryBeanClassName() {
                return MongoExtRepositoryFactoryBean.class.getName();
            }
        };
    }

    @EnableMongoRepositories(repositoryFactoryBeanClass = MongoExtRepositoryFactoryBean.class)
    private static class EnableMongoExtRepositoriesConfiguration {

    }
}
