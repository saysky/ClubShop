package com.example.mall.mapper;

import com.example.mall.entity.ArticleCategory;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (ArticleCategory)表数据库访问层
 *
 * @author makejava
 * @since 2021-09-19 15:25:03
 */
public interface ArticleCategoryMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ArticleCategory queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ArticleCategory> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ArticleCategory 实例对象
     * @return 对象列表
     */
    List<ArticleCategory> queryAll(ArticleCategory ArticleCategory);

    /**
     * 新增数据
     *
     * @param ArticleCategory 实例对象
     * @return 影响行数
     */
    int insert(ArticleCategory ArticleCategory);

    /**
     * 修改数据
     *
     * @param ArticleCategory 实例对象
     * @return 影响行数
     */
    int update(ArticleCategory ArticleCategory);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}