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
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.CloseableIterator;

import java.util.List;
import java.util.Set;

/**
 * Created by lianhao on 2017/5/15.
 */
@NoRepositoryBean
public interface MongoOperationExecutor<T> {

    MongoConverter getConverter();

    MongoOperations getOperations();

    Document executeCommand(String jsonCommand);

    Document executeCommand(Document command);

    Document executeCommand(Document command, ReadPreference readPreference);

    void executeQuery(Query query, DocumentCallbackHandler dch);

    T execute(CollectionCallback<T> action);

    CloseableIterator<T> stream(Query query);

    Set<String> getCollectionNames();

    MongoCollection<Document> getCollection();

    boolean collectionExists();

    void dropCollection();

    IndexOperations indexOps();

    ScriptOperations scriptOps();

    BulkOperations bulkOps(BulkOperations.BulkMode mode);

    GroupByResults<T> group(GroupBy groupBy);

    GroupByResults<T> group(Criteria criteria, GroupBy groupBy);

    <O> AggregationResults<O> aggregate(TypedAggregation<?> aggregation, Class<O> outputType);

    <O> MapReduceResults<O> mapReduce(String mapFunction, String reduceFunction, Class<O> entityClass);

    <O> MapReduceResults<O> mapReduce(Query query, String mapFunction, String reduceFunction, Class<O> entityClass);

    <O> MapReduceResults<O> mapReduce(Query query, String mapFunction, String reduceFunction, MapReduceOptions mapReduceOptions, Class<O> entityClass);

    GeoResults<T> geoNear(NearQuery query);

    GeoPage<T> geoNear(NearQuery query, Pageable pageable);

    T findOne(Query query);

    boolean exists(Query query);

    List<T> find(Query query);

    Page<T> find(Query query, Pageable pageable);

    T findAndModify(Query query, Update update);

    T findAndModify(Query query, Update update, FindAndModifyOptions options);

    T findAndRemove(Query query);

    long count(Query query);

    UpdateResult upsert(Query query, Update update);

    UpdateResult updateFirst(Query query, Update update);

    UpdateResult updateMulti(Query query, Update update);

    DeleteResult remove(Query query);

    List<T> findAllAndRemove(Query query);
}
