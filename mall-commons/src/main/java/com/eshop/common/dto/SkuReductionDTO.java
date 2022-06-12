package com.eshop.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/12
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class SkuReductionDTO {

    private long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
