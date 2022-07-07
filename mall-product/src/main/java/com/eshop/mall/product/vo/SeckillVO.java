package com.eshop.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ruomengjiang
 * @Date 2022/7/6
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class SeckillVO {
    private Long skuId;

    private BigDecimal seckillPrice;

    private BigDecimal seckillCount;

    private BigDecimal seckillLimit;

    private Integer seckillSort;

    private Long startTime;
    private Long endTime;
    // 随机码
    private String randCode;

    //活动id
    private Long promotionSessionId;
}
