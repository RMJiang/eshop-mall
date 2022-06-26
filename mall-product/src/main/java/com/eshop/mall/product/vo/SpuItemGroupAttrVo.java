package com.eshop.mall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/24
 * @Description : eshop-mall
 * 属性组相关的VO
 * @Version: 1.0
 */
@ToString
@Data
public class SpuItemGroupAttrVo {
    private String groupName;
    private List<Attr> baseAttrs;
}
