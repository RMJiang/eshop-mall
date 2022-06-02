package com.eshop.mall.coupon.dao;

import com.eshop.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 14:30:10
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
