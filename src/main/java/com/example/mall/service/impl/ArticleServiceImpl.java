package com.example.mall.service.impl;

import com.example.mall.entity.Article;
import com.example.mall.entity.ArticleCategory;
import com.example.mall.entity.Goods;
import com.example.mall.entity.User;
import com.example.mall.mapper.ArticleCategoryMapper;
import com.example.mall.mapper.ArticleMapper;
import com.example.mall.mapper.MallUserMapper;
import com.example.mall.service.ArticleService;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * (Article)表服务实现类
 *
 * @author makejava
 * @since 2021-09-19 15:25:03
 */
@Service("ArticleService")
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private MallUserMapper userMapper;

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Article queryById(Long id) {
        return this.articleMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Article> queryAllByLimit(int offset, int limit) {
        return this.articleMapper.queryAllByLimit(offset, limit);
    }

    @Override
    public List<Article> queryByCategoryId(Long categoryId) {
        return articleMapper.queryByCategoryId(categoryId);
    }

    /**
     * 新增数据
     *
     * @param Article 实例对象
     * @return 实例对象
     */
    @Override
    public Article insert(Article Article) {
        this.articleMapper.insert(Article);
        return Article;
    }

    /**
     * 修改数据
     *
     * @param Article 实例对象
     * @return 实例对象
     */
    @Override
    public Article update(Article Article) {
        this.articleMapper.update(Article);
        return this.queryById(Article.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.articleMapper.deleteById(id) > 0;
    }

    @Override
    public PageResult getArticlePage(PageQueryUtil pageUtil) {
        List<Article> articles = articleMapper.findArticleList(pageUtil);
        for (Article article : articles) {
            ArticleCategory articleCategory = articleCategoryMapper.queryById(article.getCategoryId());
            User user = userMapper.selectByPrimaryKey(article.getUserId());
            article.setCategoryName(articleCategory != null ? articleCategory.getName() : null);
            article.setUserName(user != null ? user.getNickName() : null);
        }
        int total = articleMapper.getTotalArticle(pageUtil);
        PageResult pageResult = new PageResult(articles, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public List<Article> getLatest(int limit) {
        return articleMapper.getLatest(limit);
    }
}