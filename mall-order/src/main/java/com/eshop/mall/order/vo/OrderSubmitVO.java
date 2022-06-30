package com.eshop.mall.order.vo;

import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/6/30
 * @Description : eshop-mall
 * 订单结算页提交的信息
 * @Version: 1.0
 */
@Data
public class OrderSubmitVO {

    // 收获地址的id
    private Long addrId;

    // 支付方式
    private Integer payType;

    // 防重Token
    private String orderToken;

    // 买家备注
    private String note;
}