package com.eshop.mall.member.exception;

/**
 * @Author ruomengjiang
 * @Date 2022/6/27
 * @Description : eshop-mall
 * 用户名存储的自定义异常
 * @Version: 1.0
 */
public class UsernameExsitException extends RuntimeException{

    public UsernameExsitException(){
        super("账号已存在");
    }
}
