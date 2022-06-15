package com.eshop.mall.product.fegin;

import com.eshop.common.dto.SkuHasStockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/15
 * @Description : eshop-mall
 * @Version: 1.0
 */
@FeignClient("mall-ware")
public interface WareSkuFeginService {

    @PostMapping("/ware/waresku/hasStock")
    List<SkuHasStockDto> getSkusHasStock(@RequestBody List<Long> skuIds);

}
