package com.eshop.mall.order.dao;

import com.eshop.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 12:33:03
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    OrderEntity getOrderByOrderSn(@Param("orderSn") String orderSn);

    void updateOrderStatus(@Param("orderSn") String orderSn, @Param("status") Integer status);
}
