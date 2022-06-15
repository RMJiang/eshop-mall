package com.eshop.mall.ware.feign;

import com.eshop.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author ruomengjiang
 * @Date 2022/6/13
 * @Description : eshop-mall
 * @Version: 1.0
 */
@FeignClient("mall-product")
public interface ProductFeignService {
    /**
     * 当然我们也可以通过网关来调用商品服务
     * @param skuId
     * @return
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
