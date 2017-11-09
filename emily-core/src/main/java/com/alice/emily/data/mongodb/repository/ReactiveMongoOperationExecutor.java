package com.alice.emily.data.mongodb.repository;

import com.mongodb.ReadPreference;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveCollectionCallback;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.ReactiveIndexOperations;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by lianhao on 2017/6/8.
 */
@NoRepositoryBean
public interface ReactiveMongoOperationExecutor<T> {

    ReactiveIndexOperations indexOps();

    Mono<Document> executeCommand(String jsonCommand);

    Mono<Document> executeCommand(Document command);

    Mono<Document> executeCommand(Document command, ReadPreference readPreference);

    Flux<T> execute(ReactiveCollectionCallback<T> action);

    Flux<String> getCollectionNames();

    MongoCollection<Document> getCollection();

    Mono<Boolean> collectionExists();

    Mono<Void> dropCollection();

    Mono<T> findOne(Query query);

    Mono<Boolean> exists(Query query);

    Flux<T> find(Query query);

    Flux<GeoResult<T>> geoNear(NearQuery near);

    Mono<T> findAndModify(Query query, Update update);

    Mono<T> findAndModify(Query query, Update update, FindAndModifyOptions options);

    Mono<T> findAndRemove(Query query);

    Mono<Long> count(Query query);

    Mono<UpdateResult> upsert(Query query, Update update);

    Mono<UpdateResult> updateFirst(Query query, Update update);

    Mono<UpdateResult> updateMulti(Query query, Update update);

    Mono<DeleteResult> remove(Query query);

    Flux<T> findAllAndRemove(Query query);

    Flux<T> tail(Query query);

    MongoConverter getConverter();

    ReactiveMongoOperations getOperations();
}
