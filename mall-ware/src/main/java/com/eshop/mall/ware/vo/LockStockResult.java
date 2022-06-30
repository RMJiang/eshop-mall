package com.eshop.mall.ware.vo;

import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/6/30
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class LockStockResult {

    private Long skuId;
    private Integer count;
    private Boolean locked;
}