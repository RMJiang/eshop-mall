package com.eshop.mall.seckill.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ruomengjiang
 * @Date 2022/7/5
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class SeckillSkuRelationEntity {

    private Long id;

    private Long promotionId;

    private Long promotionSessionId;

    private Long skuId;

    private BigDecimal seckillPrice;

    private BigDecimal seckillCount;

    private BigDecimal seckillLimit;

    private Integer seckillSort;
}
