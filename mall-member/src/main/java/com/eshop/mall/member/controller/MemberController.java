package com.eshop.mall.member.controller;

import com.alibaba.fastjson.JSON;
import com.eshop.common.exception.BizCodeEnume;
import com.eshop.common.utils.PageUtils;
import com.eshop.common.utils.R;
import com.eshop.mall.member.entity.MemberEntity;
import com.eshop.mall.member.exception.PhoneExsitExecption;
import com.eshop.mall.member.exception.UsernameExsitException;
import com.eshop.mall.member.service.MemberService;
import com.eshop.mall.member.vo.MemberLoginVO;
import com.eshop.mall.member.vo.MemberRegisterVO;
import com.eshop.mall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 会员
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-02 17:15:58
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 会员注册
     * @return
     */
    @PostMapping("/register")
    public R register(@RequestBody MemberRegisterVO vo){
        try {
            memberService.register(vo);
        }catch (UsernameExsitException exception){
            return R.error(BizCodeEnume.USERNAME_EXSIT_EXCEPTION.getCode(),
                    BizCodeEnume.USERNAME_EXSIT_EXCEPTION.getMsg());
        }catch (PhoneExsitExecption exsitExecption) {
            return R.error(BizCodeEnume.PHONE_EXSIT_EXCEPTION.getCode(),
                    BizCodeEnume.PHONE_EXSIT_EXCEPTION.getMsg());
        }catch (Exception e){
            return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),
                    BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
        }

        return R.ok();
    }

    /**
     * 用户登录
     * @param vo
     * @return
     */
    @RequestMapping("/login")
    public R login(@RequestBody MemberLoginVO vo){
        MemberEntity entity = memberService.login(vo);
        if(entity != null){
            return R.ok().put("entity", JSON.toJSONString(entity));
        }

        return R.error(BizCodeEnume.USERNAME_PHONE_VALID_EXCEPTION.getCode(),
                BizCodeEnume.USERNAME_PHONE_VALID_EXCEPTION.getMsg());
    }

    /**
     * 社交登录
     * @param vo
     * @return
     */
    @RequestMapping("/oauth2/login")
    public R socialLogin(@RequestBody SocialUser vo){
        MemberEntity entity = memberService.login(vo);

        return R.ok().put("entity", JSON.toJSONString(entity));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
