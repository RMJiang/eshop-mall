package com.eshop.mall.product.web;

import com.eshop.mall.product.service.SkuInfoService;
import com.eshop.mall.product.vo.SpuItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @Author ruomengjiang
 * @Date 2022/6/20
 * @Description : eshop-mall
 * 商品详情页
 * @Version: 1.0
 */
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String item(@PathVariable Long skuId, Model model) throws ExecutionException, InterruptedException {
        SpuItemVO spuItemVO = skuInfoService.item(skuId);
        model.addAttribute("item",spuItemVO);
        return "item";
    }

}
