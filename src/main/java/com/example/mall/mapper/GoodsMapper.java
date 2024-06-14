package com.example.mall.mapper;

import com.example.mall.entity.Goods;
import com.example.mall.entity.StockNumDTO;
import com.example.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKeyWithBLOBs(Goods record);

    int updateByPrimaryKey(Goods record);

    List<Goods> findmallGoodsList(PageQueryUtil pageUtil);

    int getTotalmallGoods(PageQueryUtil pageUtil);

    List<Goods> selectByPrimaryKeys(List<Long> goodsIds);

    List<Goods> findmallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalmallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("mallGoodsList") List<Goods> mallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);



    /**
     * 根据关键词查询商品
     *
     * @param keyword
     * @return
     */
    List<String> getSearchHelper(String keyword);

    /**
     * 获得最新商品
     * @param limit
     * @return
     */
    List<Goods> getLatestGoods(Integer limit);

    /**
     * 获得最热门商品
     * @param limit
     * @return
     */
    List<Goods> getHotGoods(Integer limit);

    int deleteById(Long id);

