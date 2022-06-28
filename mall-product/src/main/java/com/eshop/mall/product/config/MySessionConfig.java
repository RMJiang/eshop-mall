package com.eshop.mall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @Author ruomengjiang
 * @Date 2022/6/27
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Configuration
public class MySessionConfig {

    /**
     * 自定义Cookie的配置
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setDomainName("eshop.com"); // 设置session对应的一级域名
        cookieSerializer.setCookieName("eshopsession");
        return cookieSerializer;
    }

    /**
     * 对存储在Redis中的数据指定序列化的方式
     * @return
     */
    @Bean
    public RedisSerializer<Object> redisSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }
}
