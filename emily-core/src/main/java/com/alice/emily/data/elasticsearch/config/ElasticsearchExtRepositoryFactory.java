package com.alice.emily.data.elasticsearch.config;

import com.alice.emily.data.elasticsearch.repository.ElasticsearchOperationExecutor;
import com.alice.emily.data.elasticsearch.repository.SimpleElasticsearchExtRepository;
import com.alice.emily.data.elasticsearch.repository.SimpleElasticsearchOperationExecutor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.data.elasticsearch.repository.support.NumberKeyedRepository;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.UUIDElasticsearchRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFragment;

import javax.annotation.Nonnull;
import java.util.UUID;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

/**
 * Created by lianhao on 2017/6/8.
 */
public class ElasticsearchExtRepositoryFactory extends ElasticsearchRepositoryFactory {

    private final ElasticsearchOperations operations;

    public ElasticsearchExtRepositoryFactory(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
        this.operations = elasticsearchOperations;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isQueryDslRepository(metadata.getRepositoryInterface())) {
            throw new IllegalArgumentException("QueryDsl Support has not been implemented yet.");
        }
        if (Integer.class.isAssignableFrom(metadata.getIdType())
                || Long.class.isAssignableFrom(metadata.getIdType())
                || Double.class.isAssignableFrom(metadata.getIdType())) {
            return NumberKeyedRepository.class;
        } else if (metadata.getIdType() == String.class) {
            return SimpleElasticsearchRepository.class;
        } else if (metadata.getIdType() == UUID.class) {
            return UUIDElasticsearchRepository.class;
        }
        return SimpleElasticsearchExtRepository.class;
    }

    @Nonnull
    @Override
    protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
        RepositoryComposition.RepositoryFragments fragments = super.getRepositoryFragments(metadata);

        if (ElasticsearchOperationExecutor.class.isAssignableFrom(metadata.getRepositoryInterface())) {
            ElasticsearchEntityInformation<?, Object> entityInformation =
                    getEntityInformation(metadata.getDomainType());
            Object repository = getTargetRepositoryViaReflection(
                    SimpleElasticsearchOperationExecutor.class, entityInformation, operations);
            fragments = fragments.append(RepositoryFragment.implemented(repository));
        }

        return fragments;
    }

    private boolean isQueryDslRepository(Class<?> repositoryInterface) {
        return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }
}
