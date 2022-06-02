package com.eshop.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshop.common.utils.PageUtils;
import com.eshop.mall.order.entity.OrderReturnReasonEntity;

import java.util.Map;

/**
 * 退货原因
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 12:33:03
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

