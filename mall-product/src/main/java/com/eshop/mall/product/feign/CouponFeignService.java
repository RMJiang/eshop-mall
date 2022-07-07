package com.eshop.mall.product.feign;

import com.eshop.common.dto.SkuReductionDTO;
import com.eshop.common.dto.SpuBoundsDTO;
import com.eshop.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author ruomengjiang
 * @Date 2022/6/12
 * @Description : eshop-mall
 * @Version: 1.0
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveFullReductionInfo(@RequestBody SkuReductionDTO dto);

    @RequestMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsDTO spuBounds);

}
