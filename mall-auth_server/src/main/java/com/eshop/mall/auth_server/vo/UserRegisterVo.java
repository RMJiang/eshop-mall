package com.eshop.mall.auth_server.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @Author ruomengjiang
 * @Date 2022/6/26
 * @Description : eshop-mall
 * 注册用户VO对象
 * @Version: 1.0
 */
@Data
public class UserRegisterVo {
    @NotEmpty(message = "账号不能为空")
    @Length(min = 3,max = 15,message = "账号必须是3~15位")
    private String userName; // 账号

    @NotEmpty(message = "密码不能为空")
    @Length(min = 3,max = 15,message = "密码必须是3~15位")
    private String password; // 密码

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3-9][0-9]{9}$",message = "手机号不合法")
    private String phone;  // 手机号

    @NotEmpty(message = "验证码不能为空")
    private String code;  // 验证码
}
