package com.ealice.emily.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Lianhao on 2017/8/4.
 */
@Data
public class Author implements Serializable {
    /**
     * 作者id
     */
    private Long id;
    /**
     * 作者姓名
     */
    private String name;
    /**
     * 作者简介
     */
    private String remark;

    //setters and getters
    //toString

}
