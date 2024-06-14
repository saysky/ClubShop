package com.example.mall.mapper;

import com.example.mall.entity.ShoppingCartItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(ShoppingCartItem record);

    int insertSelective(ShoppingCartItem record);

    ShoppingCartItem selectByPrimaryKey(Long cartItemId);

    ShoppingCartItem selectByUserIdAndGoodsId(@Param("mallUserId") Long mallUserId, @Param("goodsId") Long goodsId);

    List<ShoppingCartItem> selectByUserId(@Param("mallUserId") Long mallUserId, @Param("number") int number);

    int selectCountByUserId(Long mallUserId);

    int updateByPrimaryKeySelective(ShoppingCartItem record);

    int updateByPrimaryKey(ShoppingCartItem record);

    int deleteBatch(List<Long> ids);
