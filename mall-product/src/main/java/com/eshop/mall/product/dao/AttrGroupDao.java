package com.eshop.mall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eshop.mall.product.entity.AttrGroupEntity;
import com.eshop.mall.product.vo.SpuItemGroupAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 21:56:44
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    List<SpuItemGroupAttrVo> getAttrgroupWithSpuId(@Param("spuId") Long spuId
            , @Param("catalogId")Long catalogId);
}
