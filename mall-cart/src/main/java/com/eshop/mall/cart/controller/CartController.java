package com.eshop.mall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author ruomengjiang
 * @Date 2022/6/27
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Controller
public class CartController {

    @RequestMapping("/cartlist")
    public String cart(){
        return "cartList";
    }

    @RequestMapping("/success")
    public String success(){
        return "success";
    }


}
