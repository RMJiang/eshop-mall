package com.eshop.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshop.common.utils.PageUtils;
import com.eshop.mall.product.entity.CategoryBrandRelationEntity;

import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 21:56:44
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrandName(Long brandId, String name);

    void updateCatelogName(Long catId, String name);
}

