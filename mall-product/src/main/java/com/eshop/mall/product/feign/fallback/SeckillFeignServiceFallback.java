package com.eshop.mall.product.feign.fallback;

import com.eshop.common.exception.BizCodeEnume;
import com.eshop.common.utils.R;
import com.eshop.mall.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author ruomengjiang
 * @Date 2022/7/12
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Slf4j
@Component
public class SeckillFeignServiceFallback implements SeckillFeignService {
    @Override
    public R getSeckillSessionBySkuId(Long skuId) {
        log.error("熔断降级 ---> SeckillFeignService:{}",skuId);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
