package com.alice.emily.data.elasticsearch.config;

import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.elasticsearch.repository.config.ElasticsearchRepositoryConfigExtension;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * Created by lianhao on 2017/5/18.
 */
public class ElasticsearchExtRepositoriesRegistrar extends AbstractRepositoryConfigurationSourceSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableElasticsearchRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableElasticsearchExtRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new ElasticsearchRepositoryConfigExtension() {
            @Override
            public String getRepositoryFactoryBeanClassName() {
                return ElasticsearchExtRepositoryFactoryBean.class.getName();
            }
        };
    }

    @EnableElasticsearchRepositories(repositoryFactoryBeanClass = ElasticsearchExtRepositoryFactoryBean.class)
    private static class EnableElasticsearchExtRepositoriesConfiguration {

    }

}