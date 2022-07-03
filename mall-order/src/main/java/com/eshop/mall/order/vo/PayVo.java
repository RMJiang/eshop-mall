package com.eshop.mall.order.vo;

import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/7/2
 * @Description : eshop-mall
 * 封装支付相关信息
 * @Version: 1.0
 */
@Data
public class PayVo {
    // 商户订单号
    private String out_trader_no;
    // 订单名称
    private String subject;
    // 付款金额
    private String total_amount;
    // 描述
    private String body;

}
