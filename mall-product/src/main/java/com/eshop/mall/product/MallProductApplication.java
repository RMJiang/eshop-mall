package com.eshop.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
/**
 * basePackages 制定Fegin接口的路径
 */
@EnableCaching
@EnableFeignClients(basePackages = "com.eshop.mall.product.fegin")
//放开注册中心
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.eshop.mall.product.dao")
@ComponentScan(basePackages = "com.eshop")
public class MallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }

}
