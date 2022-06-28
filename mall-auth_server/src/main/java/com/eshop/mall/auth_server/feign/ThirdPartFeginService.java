package com.eshop.mall.auth_server.feign;

import com.eshop.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ruomengjiang
 * @Date 2022/6/26
 * @Description : eshop-mall
 * @Version: 1.0
 */
@FeignClient("mall-third")
public interface ThirdPartFeginService {

    @GetMapping("/sms/sendcode")
    public R sendSmsCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
