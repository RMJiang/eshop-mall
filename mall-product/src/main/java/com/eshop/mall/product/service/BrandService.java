package com.eshop.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshop.common.utils.PageUtils;
import com.eshop.mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 21:56:44
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

