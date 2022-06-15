package com.eshop.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/13
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class MergeVO {
    //{ purchaseId: this.purchaseId, items: items }
    private Long purchaseId;
    private List<Long> items;
}
