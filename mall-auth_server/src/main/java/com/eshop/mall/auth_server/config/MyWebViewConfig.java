package com.eshop.mall.auth_server.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author ruomengjiang
 * @Date 2022/6/26
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Configuration
public class MyWebViewConfig implements WebMvcConfigurer {

    /**
     * 视图映射器
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("/login");
        registry.addViewController("/reg.html").setViewName("/reg");
    }

}
