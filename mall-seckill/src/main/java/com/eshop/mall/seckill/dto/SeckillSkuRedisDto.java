package com.eshop.mall.seckill.dto;

import com.eshop.mall.seckill.vo.SkuInfoVo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ruomengjiang
 * @Date 2022/7/5
 * @Description : eshop-mall
 * 封装SKU相关信息，保存到Redis中的传输对象
 * @Version: 1.0
 */
@Data
public class SeckillSkuRedisDto {
    private Long skuId;

    private BigDecimal seckillPrice;

    private BigDecimal seckillCount;

    private BigDecimal seckillLimit;

    private Integer seckillSort;

    private SkuInfoVo skuInfoVo;

    private Long startTime;
    private Long endTime;
    // 随机码
    private String randCode;

    //活动id
    private Long promotionSessionId;

}
