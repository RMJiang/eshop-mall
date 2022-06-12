package com.eshop.common.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ruomengjiang
 * @Date 2022/6/12
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class MemberPrice {
    private long id;
    private String name;
    private BigDecimal price;
}
