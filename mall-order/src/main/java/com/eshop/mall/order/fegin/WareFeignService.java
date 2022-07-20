package com.eshop.mall.order.fegin;

import com.eshop.common.utils.R;
import com.eshop.mall.order.vo.OrderItemVo;
import com.eshop.mall.order.vo.WareSkuLockVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/30
 * @Description : eshop-mall
 * @Version: 1.0
 */
@FeignClient("mall-ware")
public interface WareFeignService {

    @PostMapping("/ware/waresku/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVO vo);

    @PostMapping("/ware/waresku/updateStock")
    public R completeOrderWare(@RequestBody List<OrderItemVo> list);
}