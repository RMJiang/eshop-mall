package com.eshop.mall.seckill.service;

import com.eshop.mall.seckill.dto.SeckillSkuRedisDto;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/7/5
 * @Description : eshop-mall
 * @Version: 1.0
 */
public interface SeckillService {

    void uploadSeckillSku3Days();

    List<SeckillSkuRedisDto> getCurrentSeckillSkus();

    SeckillSkuRedisDto getSeckillSessionBySkuId(Long skuId);

    String kill(String killId, String code, Integer num);
}
