package com.example.mall.mapper;

import com.example.mall.entity.Order;
import com.example.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long orderId);

    Order selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> findmallOrderList(PageQueryUtil pageUtil);

    int getTotalmallOrders(PageQueryUtil pageUtil);

    List<Order> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);

    /**
     * 查询订单
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<Order> findByCreateTime(@Param("startTime") Date startTime,
                                 @Param("endTime") Date endTime);


