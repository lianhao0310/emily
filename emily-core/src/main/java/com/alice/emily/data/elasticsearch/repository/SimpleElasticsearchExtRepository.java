package com.alice.emily.data.elasticsearch.repository;

import com.alice.emily.core.SpringContext;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.AbstractElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.util.Lazy;

import java.io.Serializable;

/**
 * Created by lianhao on 2017/5/18.
 */
public class SimpleElasticsearchExtRepository<T, ID extends Serializable> extends AbstractElasticsearchRepository<T, ID> {

    private Lazy<GenericConversionService> conversionService = Lazy.of(SpringContext::conversionService);

    public SimpleElasticsearchExtRepository(ElasticsearchEntityInformation<T, ID> metadata,
                                            ElasticsearchOperations elasticsearchOperations) {
        super(metadata, elasticsearchOperations);
    }

    @Override
    protected String stringIdRepresentation(ID id) {
        return conversionService.get().convert(id, String.class);
    }
}
