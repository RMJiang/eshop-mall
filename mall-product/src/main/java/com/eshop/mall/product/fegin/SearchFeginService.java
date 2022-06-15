package com.eshop.mall.product.fegin;

import com.eshop.common.dto.es.SkuESModel;
import com.eshop.common.utils.R;
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
@FeignClient("mall-search")
public interface SearchFeginService {

    @PostMapping("/search/save/product")
    public R productStatusUp(@RequestBody List<SkuESModel> skuESModels);
}

