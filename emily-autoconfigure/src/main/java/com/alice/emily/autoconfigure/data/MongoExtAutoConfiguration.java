package com.alice.emily.autoconfigure.data;

import com.mongodb.Mongo;
import com.mongodb.reactivestreams.client.MongoClient;
import com.alice.emily.data.mongodb.MongoMappingConfiguration;
import com.alice.emily.data.mongodb.config.MongoExtRepositoriesRegistrar;
import com.alice.emily.data.mongodb.config.ReactiveMongoExtRepositoriesRegistrar;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactoryBean;

/**
 * Created by lianhao on 2017/6/7.
 */
@Configuration
@ConditionalOnClass(Mongo.class)
@Import({ MongoMappingConfiguration.class })
@AutoConfigureAfter({ MongoAutoConfiguration.class, MongoReactiveAutoConfiguration.class })
@AutoConfigureBefore({ MongoDataAutoConfiguration.class, MongoReactiveDataAutoConfiguration.class })
public class MongoExtAutoConfiguration {

    @Configuration
    @ConditionalOnClass({ Mongo.class, MongoRepository.class })
    @ConditionalOnMissingBean({ MongoRepositoryFactoryBean.class })
    @ConditionalOnProperty(prefix = "spring.data.mongodb.repositories",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    @Import(MongoExtRepositoriesRegistrar.class)
    @EnableMongoAuditing
    public static class ExtRepositoryConfiguration {
    }

    @Configuration
    @ConditionalOnClass({ MongoClient.class, ReactiveMongoTemplate.class })
    @ConditionalOnMissingBean({ ReactiveMongoRepositoryFactoryBean.class })
    @ConditionalOnProperty(prefix = "spring.data.mongodb.reactive-repositories",
            name = "enabled", havingValue = "true", matchIfMissing = true)
    @Import(ReactiveMongoExtRepositoriesRegistrar.class)
    @EnableMongoAuditing
    public static class ReactiveExtRepositoryConfiguration {
    }

}
