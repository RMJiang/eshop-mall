package com.eshop.mall.product.dao;

import com.eshop.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 21:56:44
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
