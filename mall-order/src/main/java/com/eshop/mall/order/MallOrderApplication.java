package com.eshop.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * basePackages 制定Fegin接口的路径
 */
@EnableFeignClients(basePackages = "com.eshop.mall.order.fegin")
//放开注册中心
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.eshop.mall.order.dao")
public class MallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallOrderApplication.class, args);
    }

}
