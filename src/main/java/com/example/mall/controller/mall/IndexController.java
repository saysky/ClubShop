package com.example.mall.controller.mall;

import com.example.mall.entity.Goods;
import com.example.mall.service.CarouselService;
import com.example.mall.service.CategoryService;
import com.example.mall.service.GoodsService;
import com.example.mall.service.IndexConfigService;
import com.example.mall.common.Constants;
import com.example.mall.common.IndexConfigTypeEnum;
import com.example.mall.controller.vo.IndexCarouselVO;
import com.example.mall.controller.vo.IndexCategoryVO;
import com.example.mall.controller.vo.IndexConfigGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private CarouselService mallCarouselService;

    @Autowired
    private IndexConfigService mallIndexConfigService;

    @Autowired
    private CategoryService mallCategoryService;

    @Autowired
    private GoodsService goodsService;


    @RequestMapping(method = RequestMethod.GET, value = {"/mall"})
    public String indexPage(HttpServletRequest request) {
        List<IndexCategoryVO> categories = mallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<IndexCarouselVO> carousels = mallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<Goods> newGoodses = goodsService.getLatestGoods(5);
        List<Goods> hotGoodses = goodsService.getHotGoods(5);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("hotGoodses", hotGoodses);//热门
        return "mall/index";
    }


}
