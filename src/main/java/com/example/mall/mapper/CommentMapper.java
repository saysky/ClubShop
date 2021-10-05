package com.example.mall.mapper;

import com.example.mall.entity.Comment;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 言曌
 * @date 2021/2/27 4:05 下午
 */
@Repository
public interface CommentMapper {

    /**
     * 根据商品ID查询
     *
     * @param goodsId
     * @return
     */
    List<Comment> findByGoodsId(Long goodsId);


    /**
     * 根据商品ID和用户ID查询
     *
     * @param userId
     * @param goodsId
     * @return
     */
    Comment findByUserIdAndGoodsId(@Param("userId") Long userId,
                                   @Param("goodsId") Long goodsId);


    /**
     * 添加评论
     *
     * @param comment
     */
    void insert(Comment comment);


}
