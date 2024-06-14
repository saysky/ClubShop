package com.example.mall.service;

import com.example.mall.entity.Article;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;

import java.util.List;

/**
 * (Article)表服务接口
 *
 * @author makejava
 * @since 2021-09-19 15:25:03
 */
public interface ArticleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Article queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Article> queryAllByLimit(int offset, int limit);

    /**
     * 查询多条数据
     *
     * @param categoryId 分类id
     * @return 对象列表
     */
    List<Article> queryByCategoryId(Long categoryId);

    /**
     * 新增数据
     *
     * @param Article 实例对象
     * @return 实例对象
     */
    Article insert(Article Article);

    /**
     * 修改数据
     *
     * @param Article 实例对象
     * @return 实例对象
     */
    Article update(Article Article);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getArticlePage(PageQueryUtil pageUtil);

    List<Article> getLatest(int limit);
