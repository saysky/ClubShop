package com.example.mall.controller.admin;

import com.example.mall.common.ServiceResultEnum;
import com.example.mall.controller.vo.UserVO;
import com.example.mall.entity.Article;
import com.example.mall.service.ArticleCategoryService;
import com.example.mall.service.ArticleService;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminArticleController {

    @Autowired
    private ArticleService mallArticleService;
    @Autowired
    private ArticleCategoryService mallCategoryService;

    @RequestMapping(method = RequestMethod.GET, value = "/article")
    public String ArticlePage(HttpServletRequest request) {
        request.setAttribute("path", "article");
        return "admin/article";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/article/edit")
    public String edit(HttpServletRequest request) {
        Article article = new Article();
        request.setAttribute("article", article);
        request.setAttribute("categoryList", mallCategoryService.queryAll());
        request.setAttribute("path", "article-edit");
        return "admin/article_edit";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/article/edit/{id}")
    public String edit(HttpServletRequest request, @PathVariable("id") Long id) {
        Article article = mallArticleService.queryById(id);
        if (article == null) {
            return "error/error_400";
        }
        request.setAttribute("article", article);
        request.setAttribute("categoryList", mallCategoryService.queryAll());
        request.setAttribute("path", "article-edit");

        return "admin/article_edit";
    }

    /**
     * ????????????
     */
    @RequestMapping(value = "/article/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("???????????????");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallArticleService.getArticlePage(pageUtil));
    }

    /**
     * ??????/??????
     */
    @RequestMapping(value = "/article/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody Article article, HttpSession session) {


        if (article.getId() == null) {
            UserVO mallUser = (UserVO) session.getAttribute("mallUser");
            if (mallUser == null) {
                return ResultGenerator.genFailResult("???????????????");
            }
            article.setUserId(mallUser.getUserId());
            article.setCreateTime(new Date());
            mallArticleService.insert(article);

        } else {
            mallArticleService.update(article);
        }
        return ResultGenerator.genSuccessResult();
    }


    /**
     * ??????
     */
    @RequestMapping(value = "/article/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody Article mallArticle) {
        mallArticleService.update(mallArticle);
        return ResultGenerator.genSuccessResult();
    }


    /**
     * ??????
     */
    @RequestMapping(method = RequestMethod.GET, value = "/article/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        Article Article = mallArticleService.queryById(id);
        if (Article == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(Article);
    }


    /**
     * ????????????
     */
    @RequestMapping(value = "/article/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("???????????????");
        }
        for (Long id : ids) {
            mallArticleService.deleteById(id);
        }
        return ResultGenerator.genSuccessResult();
    }


}