package com.eshop.mall.order.fegin;

import com.eshop.mall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/29
 * @Description : eshop-mall
 * @Version: 1.0
 */
@FeignClient("mall-member")
public interface MemberFeginService {

    @GetMapping("/member/memberreceiveaddress/{memberId}/address")
    public List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);

    @RequestMapping("/member/memberreceiveaddress/getAddressById/{id}")
    public MemberAddressVo getAddressById(@PathVariable("id") Long id);
}
