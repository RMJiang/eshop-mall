package com.eshop.mall.product.feign;

import com.eshop.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ruomengjiang
 * @Date 2022/7/6
 * @Description : eshop-mall
 * @Version: 1.0
 */
@FeignClient("mall-seckill")
public interface SeckillFeignService {

    @GetMapping("/seckill/seckillSessionBySkuId")
    public R getSeckillSessionBySkuId(@RequestParam("skuId") Long skuId);
}
