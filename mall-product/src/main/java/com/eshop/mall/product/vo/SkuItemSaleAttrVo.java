package com.eshop.mall.product.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Author ruomengjiang
 * @Date 2022/6/24
 * @Description : eshop-mall
 * 销售属性的VO
 * @Version: 1.0
 */
@ToString
@Data
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private String attrValue;
}
