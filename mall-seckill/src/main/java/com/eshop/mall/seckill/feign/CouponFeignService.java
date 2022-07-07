package com.eshop.mall.seckill.feign;

import com.eshop.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author ruomengjiang
 * @Date 2022/7/5
 * @Description : eshop-mall
 * @Version: 1.0
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/seckillsession/getLates3DaysSession")
    public R getLates3DaysSession();
}
