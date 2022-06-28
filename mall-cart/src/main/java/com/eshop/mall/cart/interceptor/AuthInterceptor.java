package com.eshop.mall.cart.interceptor;

import com.eshop.common.constant.AuthConstant;
import com.eshop.common.vo.MemberVO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author ruomengjiang
 * @Date 2022/6/28
 * @Description : eshop-mall
 * 自定义拦截器 获取当前登录用户信息
 * 通过session共享获取
 * @Version: 1.0
 */
public class AuthInterceptor implements HandlerInterceptor {

    // 本地线程对象  Map<thread,Object>
    public static ThreadLocal<MemberVO> threadLocal = new ThreadLocal();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //通过HttpSession获取当前登录的用户信息
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(AuthConstant.AUTH_SESSION_REDIS);
        if (attribute!=null){
            MemberVO memberVO = (MemberVO) attribute;
            threadLocal.set(memberVO);
            return true;
        }
        // attribute == null 则没有登录，需重定向到登录页面
        session.setAttribute(AuthConstant.AUTH_SESSION_MSG,"请先登录");
        response.sendRedirect("http://auth.eshop.com/login.html");
        return false;
    }
}
