package com.eshop.mall.order.fegin;

import com.eshop.mall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/29
 * @Description : eshop-mall
 * @Version: 1.0
 */
@FeignClient("mall-cart")
public interface CartFeginService {

    @GetMapping("/getUserCartItems")
    public List<OrderItemVo> getUserCartItems();
}
