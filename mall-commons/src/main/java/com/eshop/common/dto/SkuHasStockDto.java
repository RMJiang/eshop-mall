package com.eshop.common.dto;

import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/6/15
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class SkuHasStockDto {

    private Long skuId;

    private Boolean hasStock;
}
