package com.eshop.mall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.eshop.common.constant.CartConstant;
import com.eshop.common.utils.R;
import com.eshop.common.vo.MemberVO;
import com.eshop.mall.cart.feign.ProductFeignService;
import com.eshop.mall.cart.interceptor.AuthInterceptor;
import com.eshop.mall.cart.service.ICartService;
import com.eshop.mall.cart.vo.Cart;
import com.eshop.mall.cart.vo.CartItem;
import com.eshop.mall.cart.vo.SkuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author ruomengjiang
 * @Date 2022/6/28
 * @Description : eshop-mall
 * 购物车信息存储在redis中
 * @Version: 1.0
 */
@Service
public class ICartServiceImpl implements ICartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private ThreadPoolExecutor executor;

    /**
     * 查询出当前登录用户的所有的购物车信息
     * @return
     */
    @Override
    public Cart getCartList() {
        BoundHashOperations<String, Object, Object> operations = getCartKeyOperation();
        Set<Object> keys = operations.keys();
        Cart cart = new Cart();
        List<CartItem> list = new ArrayList<>();
        for (Object k : keys) {
            String key = (String) k;
            Object o = operations.get(key);
            String json = (String) o;
            CartItem item = JSON.parseObject(json, CartItem.class);
            list.add(item);
        }
        cart.setItems(list);
        return cart;
    }

    /**
     * 商品添加到购物车
     * @param skuId
     * @param num
     * @return
     */
    @Override
    public CartItem addCart(Long skuId, Integer num) throws Exception {
        BoundHashOperations<String, Object, Object> hashOperations = getCartKeyOperation();
        // Redis存在商品的信息，只需修改商品的数量即可
        Object o = hashOperations.get(skuId.toString());
        if(o != null){
            // 该商品已存在 修改商品的数量即可
            String json = (String) o;
            CartItem item = JSON.parseObject(json, CartItem.class);
            item.setCount(item.getCount()+num);
            hashOperations.put(skuId.toString(),JSON.toJSONString(item));
            return item;
        }
        CartItem item = new CartItem();
        CompletableFuture future1 = CompletableFuture.runAsync(()->{
            // 1.远程调用 获取商品信息
            R r = productFeignService.info(skuId);
            String skuInfoJSON = (String) r.get("skuInfoJSON");
            SkuInfoVo vo = JSON.parseObject(skuInfoJSON,SkuInfoVo.class);
            item.setCheck(true);
            item.setCount(num);
            item.setPrice(vo.getPrice());
            item.setImage(vo.getSkuDefaultImg());
            item.setSkuId(skuId);
            item.setTitle(vo.getSkuTitle());
            item.setSpuId(vo.getSpuId());
        },executor);

        CompletableFuture future2 = CompletableFuture.runAsync(()->{
            // 2.获取商品销售属性
            List<String> skuSaleAttrs = productFeignService.getSkuSaleAttrs(skuId);
            item.setSkuAttr(skuSaleAttrs);
        },executor);

        CompletableFuture.allOf(future1,future2).get();
        // 3.把数据存到Redis中
        String json = JSON.toJSONString(item);
        hashOperations.put(skuId.toString(),json);

        return item;
    }

    private BoundHashOperations<String, Object, Object> getCartKeyOperation() {
        // hash key: cart:1   skuId:cartItem
        MemberVO memberVO = AuthInterceptor.threadLocal.get();
        String cartKey = CartConstant.CART_PERFIX + memberVO.getId();
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(cartKey);
        return hashOperations;
    }
}
