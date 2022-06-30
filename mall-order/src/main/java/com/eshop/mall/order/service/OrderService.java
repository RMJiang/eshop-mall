package com.eshop.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshop.common.utils.PageUtils;
import com.eshop.mall.order.entity.OrderEntity;
import com.eshop.mall.order.vo.OrderConfirmVo;
import com.eshop.mall.order.vo.OrderResponseVO;
import com.eshop.mall.order.vo.OrderSubmitVO;

import java.util.Map;

/**
 * 订单
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 12:33:03
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder();

    OrderResponseVO submitOrder(OrderSubmitVO vo);
}

