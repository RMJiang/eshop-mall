package com.eshop.mall.member.dao;

import com.eshop.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 17:15:58
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
