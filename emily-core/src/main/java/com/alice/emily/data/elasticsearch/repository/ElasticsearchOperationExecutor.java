package com.alice.emily.data.elasticsearch.repository;

import com.alice.emily.data.elasticsearch.SearchResult;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.GetResultMapper;
import org.springframework.data.elasticsearch.core.MultiGetResultMapper;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.MoreLikeThisQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.CloseableIterator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lianhao on 2017/5/18.
 */
@NoRepositoryBean
public interface ElasticsearchOperationExecutor<T> {

    Client getClient();

    ElasticsearchConverter getConverter();

    ElasticsearchOperations getOperations();

    ElasticsearchPersistentEntity<T> getPersistentEntity();

    boolean createIndex();

    boolean createIndex(Object settings);

    boolean putMapping();

    boolean putMapping(Object mappings);

    Map getMapping();

    Map getSetting();

    T queryForObject(GetQuery query);

    T queryForObject(GetQuery query, GetResultMapper mapper);

    T queryForObject(CriteriaQuery query);

    T queryForObject(StringQuery query);

    Page<T> queryForPage(SearchQuery query);

    Page<T> queryForPage(SearchQuery query, SearchResultMapper mapper);

    Page<T> queryForPage(CriteriaQuery query);

    Page<T> queryForPage(StringQuery query);

    Page<T> queryForPage(StringQuery query, SearchResultMapper mapper);

    CloseableIterator<T> stream(CriteriaQuery query);

    CloseableIterator<T> stream(SearchQuery query);

    CloseableIterator<T> stream(SearchQuery query, SearchResultMapper mapper);

    List<T> queryForList(CriteriaQuery query);

    List<T> queryForList(StringQuery query);

    List<T> queryForList(SearchQuery query);

    List<String> queryForIds(SearchQuery query);

    long count(CriteriaQuery query);

    long count(SearchQuery query);

    LinkedList<T> multiGet(SearchQuery searchQuery);

    LinkedList<T> multiGet(SearchQuery searchQuery, MultiGetResultMapper multiGetResultMapper);

    String index(IndexQuery query);

    UpdateResponse update(UpdateQuery updateQuery);

    void bulkIndex(List<IndexQuery> queries);

    void bulkUpdate(List<UpdateQuery> queries);

    void delete(CriteriaQuery criteriaQuery);

    void delete(DeleteQuery query);

    boolean deleteIndex();

    boolean indexExists();

    boolean typeExists();

    void refresh();

    ScrolledPage<T> startScroll(long scrollTimeInMillis, SearchQuery query);

    ScrolledPage<T> startScroll(long scrollTimeInMillis, SearchQuery query, SearchResultMapper mapper);

    ScrolledPage<T> startScroll(long scrollTimeInMillis, CriteriaQuery criteriaQuery);

    ScrolledPage<T> startScroll(long scrollTimeInMillis, CriteriaQuery criteriaQuery, SearchResultMapper mapper);

    ScrolledPage<T> continueScroll(String scrollId, long scrollTimeInMillis);

    ScrolledPage<T> continueScroll(String scrollId, long scrollTimeInMillis, SearchResultMapper mapper);

    void clearScroll(String scrollId);

    Page<T> moreLikeThis(MoreLikeThisQuery query);

    Boolean addAlias(AliasQuery query);

    Boolean removeAlias(AliasQuery query);

    List<AliasMetaData> queryForAlias();

    <R> R query(SearchQuery query, ResultsExtractor<R> resultsExtractor);

    Page<SearchResult<T>> searchForResultPage(SearchQuery query);
}
