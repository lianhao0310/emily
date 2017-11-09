package com.alice.emily.autoconfigure.data;

import com.alice.emily.data.elasticsearch.ElasticsearchClientConfiguration;
import com.alice.emily.data.elasticsearch.config.ElasticsearchExtRepositoriesRegistrar;
import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;

/**
 * Created by lianhao on 2017/6/7.
 */
@Configuration
@ConditionalOnClass(Client.class)
@Import(ElasticsearchClientConfiguration.class)
@AutoConfigureAfter({ ElasticsearchAutoConfiguration.class })
@AutoConfigureBefore({ ElasticsearchDataAutoConfiguration.class })
public class ElasticsearchExtAutoConfiguration {

    @Configuration
    @ConditionalOnClass({ Client.class, ElasticsearchRepository.class })
    @ConditionalOnMissingBean(ElasticsearchRepositoryFactoryBean.class)
    @Import(ElasticsearchExtRepositoriesRegistrar.class)
    public static class ExtRepositoryConfiguration {
    }
}
