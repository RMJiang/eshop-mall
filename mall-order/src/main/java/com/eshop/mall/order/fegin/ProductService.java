package com.eshop.mall.order.fegin;

/**
 * @Author ruomengjiang
 * @Date 2022/6/2
 * @Description : eshop-mall
 * @Version: 1.0
 */

import com.eshop.common.utils.R;
import com.eshop.mall.order.vo.OrderItemSpuInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @FeignClient 指明我们要从注册中心中发现的服务的名称
 */
@FeignClient("mall-product")
public interface ProductService {

    /**
     * 需要访问的远程方法
     * @return
     */
    @GetMapping("/product/brand/all")
    public R queryAllBrand();

    @RequestMapping("/product/spuinfo/getOrderItemSpuInfoBySpuId/{spuIds}")
    public List<OrderItemSpuInfoVO> getOrderItemSpuInfoBySpuId(@PathVariable("spuIds") Long[] spuIds);

}
