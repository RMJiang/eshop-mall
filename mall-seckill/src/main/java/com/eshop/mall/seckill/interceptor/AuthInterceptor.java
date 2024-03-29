package com.eshop.mall.seckill.interceptor;

import com.eshop.common.constant.AuthConstant;
import com.eshop.common.vo.MemberVO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author ruomengjiang
 * @Date 2022/7/6
 * @Description : eshop-mall
 * 秒杀活动的拦截器 确认用户是在登录状态下操作
 * @Version: 1.0
 */
public class AuthInterceptor implements HandlerInterceptor {

    public static ThreadLocal threadLocal = new ThreadLocal();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 通过HttpSession获取当前登录的用户信息
        HttpSession session = request.getSession(false);
        Object attribute = session.getAttribute(AuthConstant.AUTH_SESSION_REDIS);
        if(attribute != null){
            MemberVO memberVO = (MemberVO) attribute;
            threadLocal.set(memberVO);
            return true;
        }
        // 如果 attribute == null 说明没有登录，那么我们就需要重定向到登录页面
        session.setAttribute(AuthConstant.AUTH_SESSION_MSG,"请先登录");
        response.sendRedirect("http://auth.eshop.com/login.html");
        return false;
    }
}
