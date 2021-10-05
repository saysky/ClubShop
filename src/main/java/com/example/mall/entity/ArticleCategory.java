package com.example.mall.entity;

import java.io.Serializable;

/**
 * (ArticleCategory)实体类
 *
 * @author makejava
 * @since 2021-09-19 15:25:03
 */
public class ArticleCategory implements Serializable {
    private static final long serialVersionUID = 238253365190531038L;
    
    private Long id;
    
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}