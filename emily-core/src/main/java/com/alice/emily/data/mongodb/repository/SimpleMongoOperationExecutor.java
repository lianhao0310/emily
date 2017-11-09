package com.alice.emily.data.mongodb.repository;

import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.util.CloseableIterator;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class SimpleMongoOperationExecutor<T, ID extends Serializable> implements MongoOperationExecutor<T> {

    private final MongoEntityInformation<T, ID> information;
    private final MongoOperations operations;

    public SimpleMongoOperationExecutor(MongoEntityInformation<T, ID> information,
                                        MongoOperations operations) {
        this.information = information;
        this.operations = operations;
    }

    @Override
    public T findOne(@Nonnull Query query) {
        return operations.findOne(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public boolean exists(@Nonnull Query query) {
        return operations.exists(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public List<T> find(@Nonnull Query query) {
        return operations.find(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public Page<T> find(@Nonnull Query query, @Nonnull Pageable pageable) {
        Query q = query.with(pageable);
        List<T> list = operations.find(q, information.getJavaType(), information.getCollectionName());
        return PageableExecutionUtils.getPage(
                list, pageable, () -> operations.count(q, information.getJavaType()));
    }

    @Override
    public T findAndModify(@Nonnull Query query, @Nonnull Update update) {
        return operations.findAndModify(query, update, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public T findAndModify(@Nonnull Query query, @Nonnull Update update, @Nonnull FindAndModifyOptions options) {
        return operations.findAndModify(query, update, options, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public DeleteResult remove(@Nonnull Query query) {
        return operations.remove(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public T findAndRemove(@Nonnull Query query) {
        return operations.findAndRemove(query, information.getJavaType());
    }

    @Override
    public List<T> findAllAndRemove(@Nonnull Query query) {
        return operations.findAllAndRemove(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public long count(@Nonnull Query query) {
        return operations.count(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public UpdateResult upsert(@Nonnull Query query, @Nonnull Update update) {
        return operations.upsert(query, update, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public UpdateResult updateFirst(@Nonnull Query query, @Nonnull Update update) {
        return operations.updateFirst(query, update, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public UpdateResult updateMulti(@Nonnull Query query, @Nonnull Update update) {
        return operations.updateMulti(query, update, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public MongoConverter getConverter() {
        return operations.getConverter();
    }

    @Override
    public MongoOperations getOperations() {
        return operations;
    }

    @Override
    public Document executeCommand(@Nonnull String jsonCommand) {
        return operations.executeCommand(jsonCommand);
    }

    @Override
    public Document executeCommand(@Nonnull Document command) {
        return operations.executeCommand(command);
    }

    @Override
    public Document executeCommand(@Nonnull Document command, @Nonnull ReadPreference readPreference) {
        return operations.executeCommand(command, readPreference);
    }

    @Override
    public void executeQuery(@Nonnull Query query, @Nonnull DocumentCallbackHandler dch) {
        operations.executeQuery(query, information.getCollectionName(), dch);
    }

    @Override
    public T execute(@Nonnull CollectionCallback<T> action) {
        return operations.execute(information.getJavaType(), action);
    }

    @Override
    public CloseableIterator<T> stream(@Nonnull Query query) {
        return operations.stream(query, information.getJavaType());
    }

    @Override
    public Set<String> getCollectionNames() {
        return operations.getCollectionNames();
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return operations.getCollection(information.getCollectionName());
    }

    @Override
    public boolean collectionExists() {
        return operations.collectionExists(information.getCollectionName());
    }

    @Override
    public IndexOperations indexOps() {
        return operations.indexOps(information.getJavaType());
    }

    @Override
    public ScriptOperations scriptOps() {
        return operations.scriptOps();
    }

    @Override
    public BulkOperations bulkOps(@Nonnull BulkOperations.BulkMode mode) {
        return operations.bulkOps(mode, information.getJavaType());
    }

    @Override
    public void dropCollection() {
        operations.dropCollection(information.getJavaType());
    }

    @Override
    public GroupByResults<T> group(@Nonnull GroupBy groupBy) {
        return operations.group(information.getCollectionName(), groupBy, information.getJavaType());
    }

    @Override
    public GroupByResults<T> group(@Nonnull Criteria criteria, @Nonnull GroupBy groupBy) {
        return operations.group(criteria, information.getCollectionName(), groupBy, information.getJavaType());
    }

    @Override
    public <O> AggregationResults<O> aggregate(@Nonnull TypedAggregation<?> aggregation, @Nonnull Class<O> outputType) {
        if (aggregation.getInputType() != null) {
            return operations.aggregate(aggregation, outputType);
        } else {
            return operations.aggregate(aggregation, information.getCollectionName(), outputType);
        }
    }

    @Override
    public <O> MapReduceResults<O> mapReduce(@Nonnull String mapFunction, @Nonnull String reduceFunction,
                                             @Nonnull Class<O> entityClass) {
        return operations.mapReduce(information.getCollectionName(), mapFunction, reduceFunction, entityClass);
    }

    @Override
    public <O> MapReduceResults<O> mapReduce(@Nonnull Query query, @Nonnull String mapFunction,
                                             @Nonnull String reduceFunction, @Nonnull Class<O> entityClass) {
        return operations.mapReduce(query, information.getCollectionName(),
                mapFunction, reduceFunction, entityClass);
    }

    @Override
    public <O> MapReduceResults<O> mapReduce(@Nonnull Query query, @Nonnull String mapFunction,
                                             @Nonnull String reduceFunction, @Nonnull MapReduceOptions mapReduceOptions,
                                             @Nonnull Class<O> entityClass) {
        return operations.mapReduce(query, information.getCollectionName(), mapFunction,
                reduceFunction, mapReduceOptions, entityClass);
    }

    @Override
    public GeoResults<T> geoNear(@Nonnull NearQuery query) {
        return operations.geoNear(query, information.getJavaType(), information.getCollectionName());
    }

    @Override
    public GeoPage<T> geoNear(@Nonnull NearQuery query, @Nonnull Pageable pageable) {
        NearQuery nq = query.with(pageable);
        GeoResults<T> geoResults = operations.geoNear(nq, information.getJavaType(), information.getCollectionName());
        return new GeoPage<>(geoResults);
    }
}