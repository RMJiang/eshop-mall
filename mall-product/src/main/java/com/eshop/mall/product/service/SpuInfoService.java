package com.eshop.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshop.common.utils.PageUtils;
import com.eshop.mall.product.entity.SpuInfoEntity;
import com.eshop.mall.product.vo.SpuInfoVO;

import java.util.Map;

/**
 * spu信息
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 21:56:44
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuInfoVO spuInfoVO);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

