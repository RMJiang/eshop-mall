package com.eshop.mall.product.vo;

import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/6/10
 * @Description : eshop-mall
 * VO: view Object
 * @Version: 1.0
 */
@Data
public class AttrVO {

    private Long attrId;
    private String attrName;
    private Integer searchType;
    private String icon;
    private String valueSelect;
    private Integer attrType;
    private Long enable;
    private Long catelogId;
    private Integer showDesc;

    /**
     * 记录规格参数对应的属性组
     */
    private Long attrGroupId;
}
