package com.example.mall.interceptor;

import com.example.mall.common.Constants;
import com.example.mall.controller.vo.ShoppingCartItemVO;
import com.example.mall.controller.vo.UserVO;
import com.example.mall.mapper.ShoppingCartItemMapper;
import com.example.mall.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * newbee-mall购物车数量处理
 */
@Component
public class CartNumberInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private ShoppingCartItemMapper mallShoppingCartItemMapper;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //购物车中的数量会更改，但是在这些接口中并没有对session中的数据做修改，这里统一处理一下
        if (null != request.getSession() && null != request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY)) {
            //如果当前为登陆状态，就查询数据库并设置购物车中的数量值
            UserVO mallUserVO = (UserVO) request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY);
            //设置购物车中的数量
            List<ShoppingCartItemVO> myShoppingCartItems = shoppingCartService.getMyShoppingCartItems(mallUserVO.getUserId());
            int sum = myShoppingCartItems.stream().mapToInt(ShoppingCartItemVO::getSellingPrice).sum();
            mallUserVO.setShopCartItemCount(myShoppingCartItems.size());
            mallUserVO.setShopCartTotalAmount(sum);
            request.getSession().setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}