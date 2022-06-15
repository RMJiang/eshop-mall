package com.eshop.mall.search.service;

import com.eshop.common.dto.es.SkuESModel;

import java.io.IOException;
import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/15
 * @Description : eshop-mall
 * @Version: 1.0
 */
public interface ElasticSearchSaveService {

    Boolean productStatusUp(List<SkuESModel> skuESModels) throws IOException;
}
