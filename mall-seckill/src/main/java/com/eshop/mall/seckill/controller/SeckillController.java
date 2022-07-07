package com.eshop.mall.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.eshop.common.utils.R;
import com.eshop.mall.seckill.dto.SeckillSkuRedisDto;
import com.eshop.mall.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/7/5
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    /**
     * 查询出当前时间类的秒杀活动及对应商品sku信息
     * @return
     */
    @GetMapping("/currentSeckillSessionSkus")
    @ResponseBody
    public R getCurrentSeckillSessionSkus(){
        List<SeckillSkuRedisDto> currentSeckillSkus = seckillService.getCurrentSeckillSkus();
        return R.ok().put("data", JSON.toJSONString(currentSeckillSkus));
    }

    /**
     * 根据skuId查询秒杀活动对应的信息
     * @param skuId
     * @return
     */
    @GetMapping("/seckillSessionBySkuId")
    @ResponseBody
    public R getSeckillSessionBySkuId(@RequestParam("skuId") Long skuId){
        SeckillSkuRedisDto dto = seckillService.getSeckillSessionBySkuId(skuId);
        return R.ok().put("data",JSON.toJSONString(dto));
    }

    /**
     * 秒杀抢购
     * http://seckill.eshop.com/seckill/kill?killId=1_9&code=887d1e76992f4a53bffd16a0d9082947&num=1
     * @return
     */
    @GetMapping("/kill")
    public String seckill(@RequestParam("killId") String killId,
                          @RequestParam("code") String code,
                          @RequestParam("num") Integer num,
                          Model model) {
        String orderSn = seckillService.kill(killId,code,num);
        model.addAttribute("orderSn",orderSn);
        return "success";
    }
}
