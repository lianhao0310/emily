package com.alice.emily.data.repo.mongo;

import com.alice.emily.data.mongodb.repository.MongoExtRepository;

/**
 * Created by lianhao on 2017/6/8.
 */
public interface DepartmentRepository extends MongoExtRepository<Department, String> {

    Department findByName(String name);
}
