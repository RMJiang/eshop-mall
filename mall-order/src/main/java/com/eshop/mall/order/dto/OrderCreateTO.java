package com.eshop.mall.order.dto;

import com.eshop.mall.order.entity.OrderEntity;
import com.eshop.mall.order.entity.OrderItemEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/30
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class OrderCreateTO {

    private OrderEntity orderEntity; // 订单信息
    private List<OrderItemEntity> orderItemEntitys; // 订单信息
}
