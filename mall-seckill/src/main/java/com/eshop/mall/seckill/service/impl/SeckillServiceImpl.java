package com.eshop.mall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.eshop.common.constant.OrderConstant;
import com.eshop.common.constant.SeckillConstant;
import com.eshop.common.dto.SeckillOrderDto;
import com.eshop.common.utils.R;
import com.eshop.common.vo.MemberVO;
import com.eshop.mall.seckill.dto.SeckillSkuRedisDto;
import com.eshop.mall.seckill.feign.CouponFeignService;
import com.eshop.mall.seckill.feign.ProductFeignService;
import com.eshop.mall.seckill.interceptor.AuthInterceptor;
import com.eshop.mall.seckill.service.SeckillService;
import com.eshop.mall.seckill.vo.SeckillSessionEntity;
import com.eshop.mall.seckill.vo.SkuInfoVo;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author ruomengjiang
 * @Date 2022/7/5
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RocketMQTemplate rocketMQTemplate;


    @Override
    public void uploadSeckillSku3Days() {
        // 1. 通过OpenFegin 远程调用Coupon服务中接口 获取未来三天的秒杀活动的商品
        R r = couponFeignService.getLates3DaysSession();
        if(r.getCode() == 0){
            // 表示查询操作成功
            String json = (String) r.get("data");
            List<SeckillSessionEntity> seckillSessionEntities = JSON.parseArray(json,SeckillSessionEntity.class);
            // 2. 上架商品  Redis数据保存
            // 缓存商品
            //  2.1 缓存每日秒杀的SKU基本信息
            saveSessionInfos(seckillSessionEntities);
            // 2.2  缓存每日秒杀的商品信息
            saveSessionSkuInfos(seckillSessionEntities);
        }

    }

    /**
     * 查询出当前时间类的秒杀活动及对应商品sku信息
     * @return
     */
    @Override
    public List<SeckillSkuRedisDto> getCurrentSeckillSkus() {
        //1.确定当前时间属于哪个秒杀活动
        long time = new Date().getTime();
        //从redis中查询所有的秒杀活动
        Set<String> keys = redisTemplate.keys(SeckillConstant.SESSION_CHACE_PREFIX + "*");
        for (String key : keys) {
            //seckill:sessions1657065600000_1657067400000
            String replace = key.replace(SeckillConstant.SESSION_CHACE_PREFIX, "");
            //1657065600000_1657067400000
            String[] s = replace.split("_");
            Long start = Long.parseLong(s[0]); //活动开始时间
            Long end = Long.parseLong(s[1]); //活动结束时间
            if(time > start && time < end){
                //当前秒杀活动 是当前时间需要参与的活动
                //取出sessionId_skuid
                List<String> range = redisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SeckillConstant.SKU_CHACE_PREFIX);
                List<String> list = ops.multiGet(range);
                if(list !=null && list.size()>0){
                    List<SeckillSkuRedisDto> collect = list.stream().map(item -> {
                        SeckillSkuRedisDto seckillSkuRedisDto = JSON.parseObject(item, SeckillSkuRedisDto.class);
                        return seckillSkuRedisDto;
                    }).collect(Collectors.toList());
                    return collect;
                }
            }
        }
        return null;
    }

    /**
     * 根据skuId查询秒杀活动对应的信息
     * @param skuId
     * @return
     */
    @Override
    public SeckillSkuRedisDto getSeckillSessionBySkuId(Long skuId) {
        //1.找到所有参与秒杀商品的sku信息
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SeckillConstant.SKU_CHACE_PREFIX);
        Set<String> keys = ops.keys();
        if(keys !=null && keys.size() > 0){
            String regx = "\\d_" + skuId;
            for (String key : keys) {
                boolean matches = Pattern.matches(regx, key);
                if (matches){
                    //找到对应sku信息
                    String json = ops.get(key);
                    SeckillSkuRedisDto dto = JSON.parseObject(json, SeckillSkuRedisDto.class);
                    return dto;
                }
            }
        }
        return null;
    }

    /**
     * 实现秒杀逻辑
     * @param killId
     * @param code
     * @param num
     * @return
     */
    @Override
    public String kill(String killId, String code, Integer num) {
        //1.根据killId获取当前秒杀商品的信息
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SeckillConstant.SKU_CHACE_PREFIX);
        String json = ops.get(killId);
        if(StringUtils.isNotBlank(json)){
            SeckillSkuRedisDto dto = JSON.parseObject(json, SeckillSkuRedisDto.class);
            //校验合法性 1.校验时效性
            Long startTime = dto.getStartTime();
            Long endTime = dto.getEndTime();
            Long now = new Date().getTime();
            if(now > startTime && now < endTime){
                //说明是在秒杀活动时间范围内的请求
                // 2.校验随机码和商品是否合法
                String randCode = dto.getRandCode();
                Long skuId = dto.getSkuId();
                String redisKillId = dto.getPromotionSessionId() + "_" + skuId;
                if(randCode.equals(code) && killId.equals(redisKillId)){
                    //随机码校验合法
                    //3.判断抢购商品数量是否合法
                    if(num <= dto.getSeckillLimit().intValue()){
                        //满足限购条件
                        //4.判断是否满足幂等性
                        //只要抢购成功 在redis中存储一条信息 userId + sessionId + skuId
                        MemberVO memberVO = (MemberVO) AuthInterceptor.threadLocal.get();
                        Long id = memberVO.getId();
                        String redisKey = id + "_" + redisKillId;
                        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), (endTime - now), TimeUnit.MILLISECONDS);
                        if(aBoolean){
                            //表示数据插入成功 是第一次操作
                            RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.SKU_STOCK_SEMAOHORE + randCode);
                            try {
                                boolean b = semaphore.tryAcquire(num, 100, TimeUnit.MILLISECONDS);
                                if(b){
                                    //表示获取到了信号量信息 秒杀成功
                                    String orderSn = UUID.randomUUID().toString().replace("-", "");
                                    //完成快速下订单操作 --->RocketMQ
                                    SeckillOrderDto orderDto = new SeckillOrderDto();
                                    orderDto.setOrderSn(orderSn);
                                    orderDto.setSkuId(skuId);
                                    orderDto.setSeckillPrice(dto.getSeckillPrice());
                                    orderDto.setMemberId(id);
                                    orderDto.setNum(num);
                                    orderDto.setPromotionSessionId(dto.getPromotionSessionId());
                                    //通过RocketMQ 发送异步消息
                                    rocketMQTemplate.sendOneWay(OrderConstant.ROCKETMQ_SECKILL_ORDER_TOPIC
                                            ,MessageBuilder.withPayload(JSON.toJSONString(orderDto)).build());
                                    return orderSn;
                                }
                            } catch (InterruptedException e) {
                                return null;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 保存每日活动的信息到Redis中
     * @param seckillSessionEntities
     */
    private void saveSessionInfos(List<SeckillSessionEntity> seckillSessionEntities) {
        for (SeckillSessionEntity seckillSessionEntity : seckillSessionEntities) {
            // 循环缓存每一个活动  key： start_endTime
            long start = seckillSessionEntity.getStartTime().getTime();
            long end = seckillSessionEntity.getEndTime().getTime();
            // 生成Key
            String key = SeckillConstant.SESSION_CHACE_PREFIX + start + "_" + end;
            Boolean flag = redisTemplate.hasKey(key);
            if(!flag){
                //这个秒杀活动在redis中不存在 还没有上架
                // 需要存储到Redis中的这个秒杀活动涉及到的相关的商品信息的SKUID
                List<String> collect = seckillSessionEntity.getRelationEntities().stream().map(item -> {
                    // 秒杀活动存储的value是sessionId_skuId
                    return item.getPromotionSessionId() + "_" + item.getSkuId().toString();
                }).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key,collect);
            }
        }
    }

    /**
     * 存储活动对应的 SKU信息
     * @param seckillSessionEntities
     */
    private void saveSessionSkuInfos(List<SeckillSessionEntity> seckillSessionEntities) {
        seckillSessionEntities.stream().forEach(session -> {
            // 循环取出每个Session，然后取出对应SkuID 封装相关的信息
            BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(SeckillConstant.SKU_CHACE_PREFIX);
            session.getRelationEntities().stream().forEach(item->{
                String skuKey = item.getPromotionSessionId()+ "_" +item.getSkuId();
                Boolean flag = redisTemplate.hasKey(skuKey);
                if(!flag){
                    SeckillSkuRedisDto dto = new SeckillSkuRedisDto();
                    // 1.获取SKU的基本信息
                    R info = productFeignService.info(item.getSkuId());
                    if(info.getCode() == 0){
                        // 表示查询成功
                        //SkuInfoVo skuInfoVo = (SkuInfoVo) info.get("skuInfo");
                        //dto.setSkuInfoVo(skuInfoVo);
                        String skuInfoJSON = (String) info.get("skuInfoJSON");
                        SkuInfoVo skuInfoVo = JSON.parseObject(skuInfoJSON, SkuInfoVo.class);
                        dto.setSkuInfoVo(skuInfoVo);
                    }
                    // 2.获取SKU的秒杀信息
                    /*dto.setSkuId(item.getSkuId());
                    dto.setSeckillPrice(item.getSeckillPrice());
                    dto.setSeckillCount(item.getSeckillCount());
                    dto.setSeckillLimit(item.getSeckillLimit());
                    dto.setSeckillSort(item.getSeckillSort());*/
                    BeanUtils.copyProperties(item,dto);
                    // 3.设置当前商品的秒杀时间
                    dto.setStartTime(session.getStartTime().getTime());
                    dto.setEndTime(session.getEndTime().getTime());
                    // 4. 随机码
                    String token = UUID.randomUUID().toString().replace("-","");
                    dto.setRandCode(token);
                    // 5. 绑定对应的活动编号
                    dto.setPromotionSessionId(item.getPromotionSessionId());
                    //分布式信号量处理 限流
                    RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.SKU_STOCK_SEMAOHORE + token);
                    //把秒杀活动的商品数量作为分布式信号量的信号量
                    semaphore.trySetPermits(item.getSeckillCount().intValue());
                    hashOps.put(skuKey,JSON.toJSONString(dto));
                }
            });
        });
    }




}
