package com.eshop.mall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/30
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class WareSkuLockVO {

    // 订单编号
    private String orderSN;

    private List<OrderItemVo> items;
}
