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
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Created by lianhao on 2017/6/8.
 */
public class SimpleReactiveMongoOperationExecutor<T, ID extends Serializable> implements ReactiveMongoOperationExecutor<T> {

    private final MongoEntityInformation<T, ID> information;
    private final ReactiveMongoOperations operations;

    public SimpleReactiveMongoOperationExecutor(MongoEntityInformation<T, ID> information,
                                                ReactiveMongoOperations operations) {
        this.information = information;
        this.operations = operations;
    }

    @Override
    public ReactiveIndexOperations indexOps() {
        return operations.indexOps(information.getCollectionName());
    }

    @Override
    public Mono<Document> executeCommand(@Nonnull String jsonCommand) {
        return operations.executeCommand(jsonCommand);
    }

    @Override
    public Mono<Document> executeCommand(@Nonnull Document command) {
        return operations.executeCommand(command);
    }

    @Override
    public Mono<Document> executeCommand(@Nonnull Document command, @Nonnull ReadPreference readPreference) {
        return operations.executeCommand(command, readPreference);
    }

    @Override
    public Flux<T> execute(@Nonnull ReactiveCollectionCallback<T> action) {
        return operations.execute(information.getJavaType(), action);
    }

    @Override
    public Flux<String> getCollectionNames() {
        return operations.getCollectionNames();
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return operations.getCollection(information.getCollectionName());
    }

    @Override
    public Mono<Boolean> collectionExists() {
        return operations.collectionExists(information.getJavaType());
    }

    @Override
    public Mono<Void> dropCollection() {
        return operations.dropCollection(information.getJavaType());
    }

    @Override
    public Mono<T> findOne(@Nonnull Query query) {
        return operations.findOne(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Mono<Boolean> exists(@Nonnull Query query) {
        return operations.exists(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Flux<T> find(@Nonnull Query query) {
        return operations.find(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Flux<GeoResult<T>> geoNear(@Nonnull NearQuery near) {
        return operations.geoNear(near, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Mono<T> findAndModify(@Nonnull Query query, @Nonnull Update update) {
        return operations.findAndModify(query, update, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Mono<T> findAndModify(@Nonnull Query query, @Nonnull Update update, @Nonnull FindAndModifyOptions options) {
        return operations.findAndModify(query, update, options, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Mono<T> findAndRemove(@Nonnull Query query) {
        return operations.findAndRemove(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Mono<Long> count(@Nonnull Query query) {
        return operations.count(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Mono<UpdateResult> upsert(@Nonnull Query query, @Nonnull Update update) {
        return operations.upsert(query, update, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Mono<UpdateResult> updateFirst(@Nonnull Query query, @Nonnull Update update) {
        return operations.updateFirst(query, update, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Mono<UpdateResult> updateMulti(@Nonnull Query query, @Nonnull Update update) {
        return operations.updateMulti(query, update, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Mono<DeleteResult> remove(@Nonnull Query query) {
        return operations.remove(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Flux<T> findAllAndRemove(@Nonnull Query query) {
        return operations.findAllAndRemove(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Flux<T> tail(@Nonnull Query query) {
        return operations.tail(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public MongoConverter getConverter() {
        return operations.getConverter();
    }

    @Override
    public ReactiveMongoOperations getOperations() {
        return operations;
    }
}
