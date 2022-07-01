package com.eshop.mall.order.utils;

import com.eshop.common.constant.OrderConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Author ruomengjiang
 * @Date 2022/7/1
 * @Description : eshop-mall
 * @Version: 1.0
 */
@RocketMQMessageListener(topic = OrderConstant.ROCKETMQ_ORDER_TOPIC,consumerGroup = "${rocketmq.consumer.group}")
@Component
public class OrderMsgConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        // TODO 订单关单的逻辑实现
        System.out.println("收到的消息：" + s);
    }
}
