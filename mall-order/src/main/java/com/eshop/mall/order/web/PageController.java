package com.eshop.mall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author ruomengjiang
 * @Date 2022/6/29
 * @Description : eshop-mall
 * 页面跳转
 * @Version: 1.0
 */
@Controller
public class PageController {

    @GetMapping("/{page}.html")
    public String goPage(@PathVariable("page") String page) {
        return page;
    }
}
