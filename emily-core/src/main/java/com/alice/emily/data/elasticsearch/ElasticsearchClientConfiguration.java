package com.alice.emily.data.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alice.emily.data.elasticsearch.mapping.ElasticsearchMappingContext;
import com.alice.emily.data.elasticsearch.mapping.JacksonEntityMapper;
import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

/**
 * Created by lianhao on 2017/5/19.
 */
@Configuration
@ConditionalOnClass({ Client.class, ElasticsearchTemplate.class })
public class ElasticsearchClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchTemplate elasticsearchTemplate(Client client,
                                                       ElasticsearchConverter converter,
                                                       ResultsMapper resultsMapper) {
        return new ElasticsearchTemplate(client, converter, resultsMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultResultMapper elasticsearchResultMapper(ElasticsearchConverter converter,
                                                         EntityMapper entityMapper) {
        return new DefaultResultMapper(converter.getMappingContext(), entityMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchConverter elasticsearchConverter(
            SimpleElasticsearchMappingContext mappingContext) {
        return new MappingElasticsearchConverter(mappingContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleElasticsearchMappingContext mappingContext() {
        return new ElasticsearchMappingContext();
    }

    @Bean
    @ConditionalOnMissingBean
    public JacksonEntityMapper elasticsearchEntityMapper(ObjectMapper objectMapper) {
        return new JacksonEntityMapper(objectMapper);
    }
}
