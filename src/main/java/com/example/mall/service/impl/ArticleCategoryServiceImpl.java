package com.example.mall.service.impl;

import com.example.mall.entity.ArticleCategory;
import com.example.mall.mapper.ArticleCategoryMapper;
import com.example.mall.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (ArticleCategory)表服务实现类
 *
 * @author makejava
 * @since 2021-09-19 15:25:03
 */
@Service("ArticleCategoryService")
public class ArticleCategoryServiceImpl implements ArticleCategoryService {
    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ArticleCategory queryById(Long id) {
        return this.articleCategoryMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ArticleCategory> queryAllByLimit(int offset, int limit) {
        return this.articleCategoryMapper.queryAllByLimit(offset, limit);
    }

    @Override
    public List<ArticleCategory> queryAll() {
        return this.articleCategoryMapper.queryAll(null);
    }

    /**
     * 新增数据
     *
     * @param ArticleCategory 实例对象
     * @return 实例对象
     */
    @Override
    public ArticleCategory insert(ArticleCategory ArticleCategory) {
        this.articleCategoryMapper.insert(ArticleCategory);
        return ArticleCategory;
    }

    /**
     * 修改数据
     *
     * @param ArticleCategory 实例对象
     * @return 实例对象
     */
    @Override
    public ArticleCategory update(ArticleCategory ArticleCategory) {
        this.articleCategoryMapper.update(ArticleCategory);
        return this.queryById(ArticleCategory.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.articleCategoryMapper.deleteById(id) > 0;
    }
