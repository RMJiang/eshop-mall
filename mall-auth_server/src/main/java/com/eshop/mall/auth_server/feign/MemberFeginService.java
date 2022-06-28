package com.eshop.mall.auth_server.feign;

import com.eshop.common.utils.R;
import com.eshop.mall.auth_server.vo.LoginVo;
import com.eshop.mall.auth_server.vo.SocialUser;
import com.eshop.mall.auth_server.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author ruomengjiang
 * @Date 2022/6/27
 * @Description : eshop-mall
 * 会员服务
 * @Version: 1.0
 */
@FeignClient("mall-member")
public interface MemberFeginService {

    @PostMapping("/member/member/register")
    public R register(@RequestBody UserRegisterVo vo);

    @RequestMapping("/member/member/login")
    public R login(@RequestBody LoginVo vo);

    @RequestMapping("/member/member/oauth2/login")
    public R socialLogin(@RequestBody SocialUser vo);

}
