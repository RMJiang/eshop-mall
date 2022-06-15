package com.eshop.mall.ware.vo;

import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/6/13
 * @Description : eshop-mall 采购项的vo数据
 * @Version: 1.0
 */
@Data
public class PurchaseItemDoneVO {

    private Long itemId;
    private Integer status;
    private String reason;
}
