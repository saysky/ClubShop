package com.example.mall.controller.mall;

import com.example.mall.common.Constants;
import com.example.mall.common.MallException;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.controller.vo.AddressVO;
import com.example.mall.controller.vo.OrderDetailVO;
import com.example.mall.controller.vo.ShoppingCartItemVO;
import com.example.mall.controller.vo.UserVO;
import com.example.mall.entity.Order;
import com.example.mall.entity.User;
import com.example.mall.service.OrderService;
import com.example.mall.service.ShoppingCartService;
import com.example.mall.service.UserService;
import com.example.mall.util.MD5Util;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class OrderController {

    @Autowired
    private ShoppingCartService mallShoppingCartService;
    @Autowired
    private OrderService mallOrderService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/orders/{orderNo}")
    public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        OrderDetailVO orderDetailVO = mallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
        if (orderDetailVO == null) {
            return "error/error_5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "mall/order-detail";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //????????????????????????
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("orderPageResult", mallOrderService.getMyOrders(pageUtil));
        request.setAttribute("path", "orders");
        return "mall/my-orders";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/saveOrder")
    public String saveOrder(AddressVO address, HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<ShoppingCartItemVO> myShoppingCartItems = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (StringUtils.isEmpty(user.getAddress().trim())) {
            //???????????????
            MallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //??????????????????????????????????????????
            MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }


        //??????????????????????????????
        String saveOrderResult = mallOrderService.saveOrder(user, address, myShoppingCartItems);
        //????????????????????????
        return "redirect:/orders/" + saveOrderResult;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/orders/{orderNo}/cancel")
    @ResponseBody
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String cancelOrderResult = mallOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String finishOrderResult = mallOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Order mallOrder = mallOrderService.getMallOrderByOrderNo(orderNo);
        //todo ????????????userId
        //todo ??????????????????
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", mallOrder.getTotalPrice());
        return "mall/pay-select";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/payPage")
    public String payOrder(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession, @RequestParam("payType") int payType) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Order mallOrder = mallOrderService.getMallOrderByOrderNo(orderNo);
        //todo ????????????userId
        //todo ??????????????????
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", mallOrder.getTotalPrice());
        if (payType == 1) {
            return "mall/alipay";
        } else {
            return "mall/wxpay";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/paySuccess")
    @ResponseBody
    public Result paySuccess(@RequestParam("orderNo") String orderNo,
                             @RequestParam("payType") int payType,
                             @RequestParam("password") String password, HttpSession session) {

        UserVO user = (UserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return ResultGenerator.genFailResult("???????????????");
        }
        User dbUser = userService.findById(user.getUserId());
        if (StringUtils.isEmpty(dbUser.getPayPasswordMd5())) {
            return ResultGenerator.genFailResult("?????????????????????");
        }

        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");

        if (!Objects.equals(dbUser.getPasswordMd5(), passwordMD5)) {
            return ResultGenerator.genFailResult("??????????????????");
        }

        String payResult = mallOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }

}
