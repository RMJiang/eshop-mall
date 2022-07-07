package com.eshop.common.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ruomengjiang
 * @Date 2022/7/7
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class SeckillOrderDto {
    private String orderSn; //订单编号
    private Long skuId; //skuId
    private Long promotionSessionId; //秒杀活动编号
    private BigDecimal seckillPrice; //秒杀价格
    private Integer num; //商品数量
    private Long memberId; //用户会员编号
}
