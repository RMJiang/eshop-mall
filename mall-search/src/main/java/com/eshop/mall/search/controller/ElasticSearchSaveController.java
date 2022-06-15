package com.eshop.mall.search.controller;

import com.eshop.common.dto.es.SkuESModel;
import com.eshop.common.exception.BizCodeEnume;
import com.eshop.common.utils.R;
import com.eshop.mall.search.service.ElasticSearchSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/15
 * @Description : eshop-mall 存储商品上架信息的 ElasticSearch 服务接口
 * 存储商城数据到 ElasticSearch 服务
 * @Version: 1.0
 */
@RestController
@RequestMapping("/search/save")
public class ElasticSearchSaveController {

    @Autowired
    private ElasticSearchSaveService service;


    /**
     * 存储商品上架信息到ElasticSearch服务的接口
     * @return
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuESModel> skuESModels){
        Boolean b = false;
        try {
            b = service.productStatusUp(skuESModels);
        } catch (IOException e) {
            // e.printStackTrace();
            //log.error("ElasticSearch商品上架错误：{}",e);
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if(b){
            return R.ok();
        }
        return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
    }
}
