package com.eshop.mall.order.utils;

import com.alibaba.fastjson.JSON;
import com.eshop.common.constant.OrderConstant;
import com.eshop.common.dto.SeckillOrderDto;
import com.eshop.mall.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author ruomengjiang
 * @Date 2022/7/7
 * @Description : eshop-mall
 * @Version: 1.0
 */
@RocketMQMessageListener(topic = OrderConstant.ROCKETMQ_SECKILL_ORDER_TOPIC,consumerGroup = "${rocketmq.consumer.group}")
@Component
public class SeckillOrderMsgConsumer implements RocketMQListener<String> {

    @Autowired
    OrderService orderService;

    @Override
    public void onMessage(String s) {
        // 秒杀成功快速下单
        System.out.println("秒杀服务收到的消息：" + s);
        SeckillOrderDto orderDto = JSON.parseObject(s,SeckillOrderDto.class);
        orderService.quickCreateOrder(orderDto);
    }
}
