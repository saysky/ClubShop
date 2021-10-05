package com.example.mall.controller.club;

import com.example.mall.common.Constants;
import com.example.mall.common.IndexConfigTypeEnum;
import com.example.mall.controller.vo.IndexCarouselVO;
import com.example.mall.controller.vo.IndexCategoryVO;
import com.example.mall.controller.vo.IndexConfigGoodsVO;
import com.example.mall.entity.Article;
import com.example.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 言曌
 * @date 2021/9/12 10:25 上午
 */
@Controller
public class ClubController {

    @Autowired
    private CategoryService mallCategoryService;

    @Autowired
    private CarouselService mallCarouselService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ArticleCategoryService categoryService;


    @RequestMapping({"/", "/index"})
    public String index(HttpServletRequest request) {
        List<IndexCategoryVO> categories = mallCategoryService.getCategoriesForIndex();
        request.setAttribute("categories", categories);

        List<IndexCarouselVO> carousels = mallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        request.setAttribute("carousels", carousels);//分类数据


        request.setAttribute("articleList", articleService.getLatest(8));
        request.setAttribute("goodsList", goodsService.getLatestGoods(8));

        return "club/index";
    }

    @RequestMapping("/article/{id}")
    public String detail(@PathVariable("id") Long id,
                         HttpServletRequest request) {

        List<IndexCategoryVO> categories = mallCategoryService.getCategoriesForIndex();
        request.setAttribute("categories", categories);

        Article article = articleService.queryById(id);
        request.setAttribute("article", article);

        request.setAttribute("category", categoryService.queryById(article.getCategoryId()));

        return "club/detail";
    }


    @RequestMapping("/category/{id}")
    public String category(@PathVariable("id") Long id,
                           HttpServletRequest request) {

        List<IndexCategoryVO> categories = mallCategoryService.getCategoriesForIndex();
        request.setAttribute("categories", categories);

        List<Article> articleList = articleService.queryByCategoryId(id);
        request.setAttribute("articleList", articleList);

        request.setAttribute("category", categoryService.queryById(id));
        return "club/category";
    }

}
