package com.eshop.mall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshop.mall.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 17:17:52
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long getSkuStock(@Param("skuId") Long skuId);

    List<WareSkuEntity> listHashStock(@Param("skuId") Long skuId);

    Integer lockSkuStock(@Param("skuId")Long skuId, @Param("wareId")Long wareId, @Param("count")Integer count);
}
