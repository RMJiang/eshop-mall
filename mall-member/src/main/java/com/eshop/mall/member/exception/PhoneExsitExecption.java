package com.eshop.mall.member.exception;

/**
 * @Author ruomengjiang
 * @Date 2022/6/27
 * @Description : eshop-mall
 * 手机号存储的自定义异常
 * @Version: 1.0
 */
public class PhoneExsitExecption extends RuntimeException{

    public PhoneExsitExecption(){
        super("手机号存在");
    }
}
