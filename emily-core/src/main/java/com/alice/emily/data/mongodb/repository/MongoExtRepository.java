package com.alice.emily.data.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by lianhao on 2017/5/15.
 */
@NoRepositoryBean
public interface MongoExtRepository<T, ID extends Serializable>
        extends MongoRepository<T, ID>, MongoOperationExecutor<T> {
}
