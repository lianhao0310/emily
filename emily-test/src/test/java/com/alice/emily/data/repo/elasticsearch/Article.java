package com.alice.emily.data.repo.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Created by lianhao on 2017/6/8.
 */
@Data
@Document(indexName = "test", type = "article")
public class Article {

    @Id
    private String id;

    private String author;

    private String title;

    @Field(index = false, type = FieldType.text)
    private String content;

    private List<Comment> comments;
}
