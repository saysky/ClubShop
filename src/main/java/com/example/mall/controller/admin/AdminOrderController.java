package com.example.mall.controller.admin;

import com.example.mall.common.ServiceResultEnum;
import com.example.mall.controller.vo.OrderItemVO;
import com.example.mall.entity.Order;
import com.example.mall.service.OrderService;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private OrderService mallOrderService;

    @RequestMapping(method = RequestMethod.GET, value = "/orders")
    public String ordersPage(HttpServletRequest request) {
        request.setAttribute("path", "orders");
        return "admin/order";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/orders/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallOrderService.getMallOrdersPage(pageUtil));
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/orders/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody Order mallOrder) {
        if (Objects.isNull(mallOrder.getTotalPrice())
                || Objects.isNull(mallOrder.getOrderId())
                || mallOrder.getOrderId() < 1
                || mallOrder.getTotalPrice() < 1
                || StringUtils.isEmpty(mallOrder.getUserAddress())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = mallOrderService.updateOrderInfo(mallOrder);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order-items/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        List<OrderItemVO> orderItems = mallOrderService.getOrderItems(id);
        if (!CollectionUtils.isEmpty(orderItems)) {
            return ResultGenerator.genSuccessResult(orderItems);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    /**
     * 配货
     */
    @RequestMapping(value = "/orders/checkDone", method = RequestMethod.POST)
    @ResponseBody
    public Result checkDone(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = mallOrderService.checkDone(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 出库
     */
    @RequestMapping(value = "/orders/checkOut", method = RequestMethod.POST)
    @ResponseBody
    public Result checkOut(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = mallOrderService.checkOut(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/orders/close", method = RequestMethod.POST)
    @ResponseBody
    public Result closeOrder(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = mallOrderService.closeOrder(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 销售统计
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistics")
    public String statistics(String startDate,
                             String endDate,
                             HttpServletRequest request) throws ParseException {
        Date start = null;
        Date end = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!StringUtils.isEmpty(startDate)) {
            start = sdf.parse(startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            end = sdf.parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(end);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            end = calendar.getTime();
        }

        List<Order> orderList = mallOrderService.findByCreateTime(start, end);
//        订单状态:0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.手动关闭 -2.超时关闭 -3.商家关闭

        // 订单数
        Long orderCount = orderList.stream().filter(p -> p.getOrderStatus() > 1).count();
        // 支付金额
        Long priceTotal = orderList.stream().filter(p -> p.getOrderStatus() > 1).mapToLong(p -> p.getTotalPrice()).sum();
        request.setAttribute("path", "statistics");

        request.setAttribute("orderCount", orderCount);
        request.setAttribute("priceTotal", priceTotal);


        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);


        return "admin/statistics";
    }

}