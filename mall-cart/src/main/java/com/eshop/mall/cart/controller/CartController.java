package com.eshop.mall.cart.controller;

import com.eshop.mall.cart.service.ICartService;
import com.eshop.mall.cart.vo.Cart;
import com.eshop.mall.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/27
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Controller
public class CartController {

    @Autowired
    private ICartService cartService;

    /**
     * 获取当前登录用户选中的商品信息 购物车中
     * @return
     */
    @GetMapping(value = "/getUserCartItems" )
    @ResponseBody
    public List<CartItem> getUserCartItems(){
        return cartService.getUserCartItems();
    }

    /**
     * 购物车界面
     * @param model
     * @return
     */
    @GetMapping("/cart_list")
    public String queryCartList(Model model){
        Cart cart = cartService.getCartList();
        model.addAttribute("cart",cart);
        return "cartList";
    }

    /**
     * 成功加入购物车
     * @param skuId
     * @param num
     * @param model
     * @return
     */
    @GetMapping("/addCart")
    public String addCart(@RequestParam("skuId") Long skuId
            , @RequestParam("num") Integer num
            , Model model){
        CartItem item = null;
        try {
            item = cartService.addCart(skuId,num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("item",item);
        return "success";
    }

}
