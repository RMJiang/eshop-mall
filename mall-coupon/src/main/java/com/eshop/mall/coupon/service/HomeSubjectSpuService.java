package com.eshop.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshop.common.utils.PageUtils;
import com.eshop.mall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * 专题商品
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 14:30:10
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

