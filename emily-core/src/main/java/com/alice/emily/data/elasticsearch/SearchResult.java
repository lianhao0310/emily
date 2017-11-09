package com.alice.emily.data.elasticsearch;

import lombok.Value;
import org.elasticsearch.search.SearchHit;

import java.io.Serializable;

/**
 * Created by lianhao on 2017/5/24.
 */
@Value
public class SearchResult<T> implements Serializable {

    private T content;

    private SearchHit hit;

}
