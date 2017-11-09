package com.alice.emily.data.elasticsearch.repository;

import com.google.common.collect.Streams;
import com.alice.emily.core.SpringBeans;
import com.alice.emily.data.elasticsearch.SearchResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.search.SearchHits;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.GetResultMapper;
import org.springframework.data.elasticsearch.core.MultiGetResultMapper;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.MoreLikeThisQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lianhao on 2017/5/18.
 */
public class SimpleElasticsearchOperationExecutor<T, ID extends Serializable> implements ElasticsearchOperationExecutor<T> {

    private final ElasticsearchEntityInformation<T, ID> information;
    private final ElasticsearchOperations operations;
    private final ResultsMapper resultsMapper;

    public SimpleElasticsearchOperationExecutor(ElasticsearchEntityInformation<T, ID> information,
                                                ElasticsearchOperations operations) {
        this.information = information;
        this.operations = operations;
        this.resultsMapper = SpringBeans.getBean(ResultsMapper.class);
    }

    @Override
    public Client getClient() {
        return operations.getClient();
    }

    @Override
    public ElasticsearchConverter getConverter() {
        return operations.getElasticsearchConverter();
    }

    @Override
    public ElasticsearchOperations getOperations() {
        return operations;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ElasticsearchPersistentEntity<T> getPersistentEntity() {
        return operations.getPersistentEntityFor(information.getJavaType());
    }

    @Override
    public boolean createIndex() {
        return operations.createIndex(information.getIndexName());
    }

    @Override
    public boolean createIndex(@Nonnull Object settings) {
        return operations.createIndex(information.getIndexName(), settings);
    }

    @Override
    public boolean putMapping() {
        return operations.putMapping(information.getJavaType());
    }

    @Override
    public boolean putMapping(@Nonnull Object mappings) {
        return operations.putMapping(information.getJavaType(), mappings);
    }

    @Override
    public Map getMapping() {
        return operations.getMapping(information.getJavaType());
    }

    @Override
    public Map getSetting() {
        return operations.getSetting(information.getJavaType());
    }

    @Override
    public T queryForObject(@Nonnull GetQuery query) {
        return operations.queryForObject(query, information.getJavaType());
    }

    @Override
    public T queryForObject(@Nonnull GetQuery query, @Nonnull GetResultMapper mapper) {
        return operations.queryForObject(query, information.getJavaType(), mapper);
    }

    @Override
    public T queryForObject(@Nonnull CriteriaQuery query) {
        return operations.queryForObject(query, information.getJavaType());
    }

    @Override
    public T queryForObject(@Nonnull StringQuery query) {
        return operations.queryForObject(query, information.getJavaType());
    }

    @Override
    public Page<T> queryForPage(@Nonnull SearchQuery query) {
        return operations.queryForPage(query, information.getJavaType());
    }

    @Override
    public Page<T> queryForPage(@Nonnull SearchQuery query, @Nonnull SearchResultMapper mapper) {
        return operations.queryForPage(query, information.getJavaType(), mapper);
    }

    @Override
    public Page<T> queryForPage(@Nonnull CriteriaQuery query) {
        return operations.queryForPage(query, information.getJavaType());
    }

    @Override
    public Page<T> queryForPage(@Nonnull StringQuery query) {
        return operations.queryForPage(query, information.getJavaType());
    }

    @Override
    public Page<T> queryForPage(@Nonnull StringQuery query, @Nonnull SearchResultMapper mapper) {
        return operations.queryForPage(query, information.getJavaType(), mapper);
    }

    @Override
    public CloseableIterator<T> stream(@Nonnull CriteriaQuery query) {
        return operations.stream(query, information.getJavaType());
    }

    @Override
    public CloseableIterator<T> stream(@Nonnull SearchQuery query) {
        return operations.stream(query, information.getJavaType());
    }

    @Override
    public CloseableIterator<T> stream(@Nonnull SearchQuery query, @Nonnull SearchResultMapper mapper) {
        return operations.stream(query, information.getJavaType(), mapper);
    }

    @Override
    public List<T> queryForList(@Nonnull CriteriaQuery query) {
        return operations.queryForList(query, information.getJavaType());
    }

    @Override
    public List<T> queryForList(@Nonnull StringQuery query) {
        return operations.queryForList(query, information.getJavaType());
    }

    @Override
    public List<T> queryForList(@Nonnull SearchQuery query) {
        return operations.queryForList(query, information.getJavaType());
    }

    @Override
    public List<String> queryForIds(@Nonnull SearchQuery query) {
        return operations.queryForIds(query);
    }

    @Override
    public long count(@Nonnull CriteriaQuery query) {
        if (CollectionUtils.isEmpty(query.getIndices())) {
            return operations.count(query, information.getJavaType());
        } else {
            return operations.count(query);
        }
    }

    @Override
    public long count(@Nonnull SearchQuery query) {
        if (CollectionUtils.isEmpty(query.getIndices())) {
            return operations.count(query, information.getJavaType());
        } else {
            return operations.count(query);
        }
    }

    @Override
    public LinkedList<T> multiGet(@Nonnull SearchQuery searchQuery) {
        return operations.multiGet(searchQuery, information.getJavaType());
    }

    @Override
    public LinkedList<T> multiGet(@Nonnull SearchQuery searchQuery, @Nonnull MultiGetResultMapper multiGetResultMapper) {
        return operations.multiGet(searchQuery, information.getJavaType(), multiGetResultMapper);
    }

    @Override
    public String index(@Nonnull IndexQuery query) {
        return operations.index(query);
    }

    @Override
    public UpdateResponse update(@Nonnull UpdateQuery updateQuery) {
        return operations.update(updateQuery);
    }

    @Override
    public void bulkIndex(@Nonnull List<IndexQuery> queries) {
        operations.bulkIndex(queries);
    }

    @Override
    public void bulkUpdate(@Nonnull List<UpdateQuery> queries) {
        operations.bulkUpdate(queries);
    }

    @Override
    public void delete(@Nonnull CriteriaQuery criteriaQuery) {
        operations.delete(criteriaQuery, information.getJavaType());
    }

    @Override
    public void delete(@Nonnull DeleteQuery query) {
        if (StringUtils.hasText(query.getIndex())) {
            operations.delete(query);
        } else {
            operations.delete(query, information.getJavaType());
        }
    }

    @Override
    public boolean deleteIndex() {
        return operations.deleteIndex(information.getJavaType());
    }

    @Override
    public boolean indexExists() {
        return operations.indexExists(information.getJavaType());
    }

    @Override
    public boolean typeExists() {
        return operations.typeExists(information.getIndexName(), getPersistentEntity().getIndexType());
    }

    @Override
    public void refresh() {
        operations.refresh(information.getJavaType());
    }

    @Override
    public ScrolledPage<T> startScroll(long scrollTimeInMillis, @Nonnull SearchQuery query) {
        return (ScrolledPage<T>) operations.startScroll(scrollTimeInMillis, query, information.getJavaType());
    }

    @Override
    public ScrolledPage<T> startScroll(long scrollTimeInMillis, @Nonnull SearchQuery query, @Nonnull SearchResultMapper mapper) {
        return (ScrolledPage<T>) operations.startScroll(scrollTimeInMillis, query, information.getJavaType(), mapper);
    }

    @Override
    public ScrolledPage<T> startScroll(long scrollTimeInMillis, @Nonnull CriteriaQuery criteriaQuery) {
        return (ScrolledPage<T>) operations.startScroll(scrollTimeInMillis, criteriaQuery, information.getJavaType());
    }

    @Override
    public ScrolledPage<T> startScroll(long scrollTimeInMillis, @Nonnull CriteriaQuery criteriaQuery, @Nonnull SearchResultMapper mapper) {
        return (ScrolledPage<T>) operations.startScroll(scrollTimeInMillis, criteriaQuery, information.getJavaType(), mapper);
    }

    @Override
    public ScrolledPage<T> continueScroll(@Nonnull String scrollId, long scrollTimeInMillis) {
        return (ScrolledPage<T>) operations.continueScroll(scrollId, scrollTimeInMillis, information.getJavaType());
    }

    @Override
    public ScrolledPage<T> continueScroll(@Nonnull String scrollId, long scrollTimeInMillis, @Nonnull SearchResultMapper mapper) {
        return (ScrolledPage<T>) operations.continueScroll(scrollId, scrollTimeInMillis, information.getJavaType(), mapper);
    }

    @Override
    public void clearScroll(@Nonnull String scrollId) {
        operations.clearScroll(scrollId);
    }

    @Override
    public Page<T> moreLikeThis(@Nonnull MoreLikeThisQuery query) {
        return operations.moreLikeThis(query, information.getJavaType());
    }

    @Override
    public Boolean addAlias(@Nonnull AliasQuery query) {
        return operations.addAlias(query);
    }

    @Override
    public Boolean removeAlias(@Nonnull AliasQuery query) {
        return operations.removeAlias(query);
    }

    @Override
    public List<AliasMetaData> queryForAlias() {
        return operations.queryForAlias(information.getIndexName());
    }

    @Override
    public <R> R query(@Nonnull SearchQuery query, @Nonnull ResultsExtractor<R> resultsExtractor) {
        return operations.query(query, resultsExtractor);
    }

    @Override
    public Page<SearchResult<T>> searchForResultPage(@Nonnull SearchQuery query) {
        setPersistentEntityIndexAndType(query);
        return operations.query(query, response -> getSearchResults(query.getPageable(), response));
    }

    private Page<SearchResult<T>> getSearchResults(Pageable pageable, SearchResponse response) {
        AggregatedPage<T> page = resultsMapper.mapResults(response, information.getJavaType(), pageable);
        SearchHits hits = response.getHits();
        List<SearchResult<T>> searchResults = Streams
                .zip(page.getContent().stream(),
                        Arrays.stream(hits.getHits()),
                        SearchResult::new)
                .collect(Collectors.toList());
        return new AggregatedPageImpl<>(searchResults, pageable, hits.getTotalHits(), page.getAggregations());
    }

    private void setPersistentEntityIndexAndType(Query query) {
        ElasticsearchPersistentEntity entity = getPersistentEntity();
        if (query.getIndices().isEmpty()) {
            query.addIndices(entity.getIndexName());
        }
        if (query.getTypes().isEmpty()) {
            query.addTypes(entity.getIndexType());
        }
    }
}
