package com.ealice.emily.example.repository;

import com.ealice.emily.example.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by Lianhao on 2017/8/4.
 */
//泛型的参数分别是实体类型和主键类型
public interface ArticleSearchRepository extends ElasticsearchRepository<Article, Long> {
}
