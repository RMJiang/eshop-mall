package com.eshop.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/13
 * @Description : eshop-mall 采购单的vo数据
 * @Version: 1.0
 */
@Data
public class PurchaseDoneVO {
    private Long id;

    private List<PurchaseItemDoneVO> items;
}
