package com.eshop.mall.seckill.schedule;

import com.eshop.mall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author ruomengjiang
 * @Date 2022/7/5
 * @Description : eshop-mall
 * 定时上架秒杀商品信息
 * @Version: 1.0
 */
@Slf4j
@Component
public class SeckillSkuSchedule {

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedissonClient redissonClient;

    /**
     *
     */
    @Async
    @Scheduled(cron = "0 * * * * *")
    public void uploadSeckillSku3Days(){
        log.info("秒杀商品上架");
        //分布式锁
        RLock lock = redissonClient.getLock("seckill:upload:lock");
        lock.lock(10, TimeUnit.SECONDS);
        try {
            // 调用上架商品的方法
            seckillService.uploadSeckillSku3Days();
        }catch (Exception e){
            lock.unlock();
        }
    }

}
