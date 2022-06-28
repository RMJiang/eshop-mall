package com.eshop.mall.product.dao;

import com.eshop.mall.product.entity.SkuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku信息
 * 
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 21:56:44
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {

    List<String> getSkuSaleAttrs(@Param("skuId") Long skuId);
}
