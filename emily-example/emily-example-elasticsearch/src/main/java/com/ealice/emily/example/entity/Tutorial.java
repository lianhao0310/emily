package com.ealice.emily.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Lianhao on 2017/8/4.
 */
@Data
public class Tutorial implements Serializable {
    private Long id;
    private String name;//教程名称

    //setters and getters
    //toString
}
