package com.alice.emily.data.mongodb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by lianhao on 2017/6/8.
 */
@NoRepositoryBean
public interface ReactiveMongoExtRepository<T, ID extends Serializable>
        extends ReactiveMongoRepository<T, ID>, ReactiveMongoOperationExecutor<T> {
}
