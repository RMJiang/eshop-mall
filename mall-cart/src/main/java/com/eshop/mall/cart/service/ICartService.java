package com.eshop.mall.cart.service;

import com.eshop.mall.cart.vo.Cart;
import com.eshop.mall.cart.vo.CartItem;

/**
 * @Author ruomengjiang
 * @Date 2022/6/28
 * @Description : eshop-mall
 * 购物车Service接口
 * @Version: 1.0
 */
public interface ICartService {
    public Cart getCartList();

    CartItem addCart(Long skuId, Integer num) throws Exception;
}
