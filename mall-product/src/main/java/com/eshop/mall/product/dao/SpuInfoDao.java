package com.eshop.mall.product.dao;

import com.eshop.mall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 21:56:44
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updateSpuStatusUp(@Param("spuId") Long spuId,@Param("code") int code);
}
