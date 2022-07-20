package com.eshop.mall.order.utils;

import com.eshop.common.constant.OrderConstant;
import com.eshop.mall.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderService orderService;

    @Override
    public void onMessage(String orderSn) {
        // TODO 订单关单的逻辑实现
        System.out.println("收到的消息：" + orderSn);
        //orderService.handleOrderComplete(orderSn);
    }
}
