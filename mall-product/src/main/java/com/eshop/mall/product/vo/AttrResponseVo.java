package com.eshop.mall.product.vo;

import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/6/10
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class AttrResponseVo extends AttrVO{

    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
