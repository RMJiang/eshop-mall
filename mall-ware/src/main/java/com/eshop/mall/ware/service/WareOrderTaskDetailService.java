package com.eshop.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshop.common.utils.PageUtils;
import com.eshop.mall.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 17:17:52
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

