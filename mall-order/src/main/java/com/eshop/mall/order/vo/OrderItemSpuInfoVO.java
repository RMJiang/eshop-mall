package com.eshop.mall.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author ruomengjiang
 * @Date 2022/6/30
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class OrderItemSpuInfoVO implements Serializable {
    private Long id;
    private String spuName;
    private String spuDescription;
    private long catalogId;
    private String catalogName;
    private long brandId;
    private String brandName;
    private String img;
}