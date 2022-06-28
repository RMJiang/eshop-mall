package com.eshop.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eshop.common.utils.PageUtils;
import com.eshop.mall.member.entity.MemberEntity;
import com.eshop.mall.member.vo.MemberLoginVO;
import com.eshop.mall.member.vo.MemberRegisterVO;
import com.eshop.mall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 17:15:58
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVO vo);

    MemberEntity login(MemberLoginVO vo);

    MemberEntity login(SocialUser vo);
}

