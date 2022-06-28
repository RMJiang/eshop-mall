package com.eshop.mall.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.eshop.common.constant.AuthConstant;
import com.eshop.common.utils.HttpUtils;
import com.eshop.common.utils.R;
import com.eshop.common.vo.MemberVO;
import com.eshop.mall.auth_server.feign.MemberFeginService;
import com.eshop.mall.auth_server.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ruomengjiang
 * @Date 2022/6/27
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Controller
public class OAuth2Controller {

    @Autowired
    private MemberFeginService memberFeginService;

    @RequestMapping("/oauth2/weibo/success")
    public String weiboOAuth(@RequestParam("code") String code
            , HttpSession session, HttpServletResponse response) throws Exception {
        System.out.println("code = " + code);
        Map<String,String> body = new HashMap<>();
        body.put("client_id","2407858920");
        body.put("client_secret","3abb12a3af97ec06760e8df52042dc62");
        body.put("grant_type","authorization_code");
        body.put("redirect_uri","http://auth.eshop.com/oauth2/weibo/success");
        body.put("code",code);
        // 根据Code获取对应的Token信息
        HttpResponse post = HttpUtils.doPost("https://api.weibo.com"
                , "/oauth2/access_token"
                , "post"
                , new HashMap<>()
                , null
                , body
        );
        int statusCode = post.getStatusLine().getStatusCode();
        if(statusCode != 200){
            // 说明获取Token失败,就调回到登录页面
            return "redirect:http://auth.eshop.com/login.html";
        }
        // 说明获取Token信息成功
        String json = EntityUtils.toString(post.getEntity());
        SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
        R r = memberFeginService.socialLogin(socialUser);
        if(r.getCode() != 0){
            // 登录错误
            return "redirect:http://auth.eshop.com/login.html";
        }
        String entityJson = (String) r.get("entity");
        MemberVO memberVO = JSON.parseObject(entityJson, MemberVO.class);
        session.setAttribute(AuthConstant.AUTH_SESSION_REDIS,memberVO);
        // 注册成功就需要调整到商城的首页
        return "redirect:http://mall.eshop.com/home";
    }

}
