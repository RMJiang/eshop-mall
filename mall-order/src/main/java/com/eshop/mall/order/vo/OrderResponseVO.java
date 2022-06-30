package com.eshop.mall.order.vo;

import com.eshop.mall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/6/30
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class OrderResponseVO {
    private OrderEntity orderEntity;
    private Integer code; // 0 表示成功  其他表示失败
}
