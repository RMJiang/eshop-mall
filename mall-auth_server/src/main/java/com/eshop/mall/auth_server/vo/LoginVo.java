package com.eshop.mall.auth_server.vo;

import lombok.Data;

/**
 * @Author ruomengjiang
 * @Date 2022/6/27
 * @Description : eshop-mall
 * 登录用户vo对象
 * @Version: 1.0
 */
@Data
public class LoginVo {
    private String userName;
    private String password;
}
