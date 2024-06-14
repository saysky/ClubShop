package com.example.mall.service;

import com.example.mall.entity.ArticleCategory;
import java.util.List;

/**
 * (ArticleCategory)表服务接口
 *
 * @author makejava
 * @since 2021-09-19 15:25:03
 */
public interface ArticleCategoryService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ArticleCategory queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ArticleCategory> queryAllByLimit(int offset, int limit);


    List<ArticleCategory> queryAll();

    /**
     * 新增数据
     *
     * @param ArticleCategory 实例对象
     * @return 实例对象
     */
    ArticleCategory insert(ArticleCategory ArticleCategory);

    /**
     * 修改数据
     *
     * @param ArticleCategory 实例对象
     * @return 实例对象
     */
    ArticleCategory update(ArticleCategory ArticleCategory);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

