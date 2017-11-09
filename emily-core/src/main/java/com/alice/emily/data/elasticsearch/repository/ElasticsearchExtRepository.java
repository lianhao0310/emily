package com.alice.emily.data.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by lianhao on 2017/5/18.
 */
@NoRepositoryBean
public interface ElasticsearchExtRepository<T, ID extends Serializable>
        extends ElasticsearchRepository<T, ID>, ElasticsearchOperationExecutor<T> {
}
