package com.eshop.mall.member.vo;

import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/6/27
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class MemberRegisterVO {
    private String userName; // 账号
    private String password; // 密码
    private String phone;  // 手机号
}
