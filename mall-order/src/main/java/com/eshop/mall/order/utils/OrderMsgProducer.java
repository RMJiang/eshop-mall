package com.eshop.mall.order.utils;

import com.eshop.common.constant.OrderConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author ruomengjiang
 * @Date 2022/7/1
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Component
public class OrderMsgProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendOrderMessage(String orderSN){

        rocketMQTemplate.syncSend(OrderConstant.ROCKETMQ_ORDER_TOPIC, MessageBuilder.withPayload(orderSN).build(),5000,4);
    }
}
