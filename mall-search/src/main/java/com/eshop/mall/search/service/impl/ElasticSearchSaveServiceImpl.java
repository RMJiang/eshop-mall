package com.eshop.mall.search.service.impl;

import com.eshop.common.dto.es.SkuESModel;
import com.eshop.mall.search.config.MallElasticSearchConfiguration;
import com.eshop.mall.search.constant.ESConstant;
import com.eshop.mall.search.service.ElasticSearchSaveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/15
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Service
public class ElasticSearchSaveServiceImpl implements ElasticSearchSaveService {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 实现上架数据存储到ES的操作
     * @param skuESModels
     * @return
     * @throws IOException
     */
    @Override
    public Boolean productStatusUp(List<SkuESModel> skuESModels) throws IOException {
        // 创建对应索引库 --》 创建对应的Product的映射
        // BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuESModel skuESModel : skuESModels) {
            // 绑定对应的索引库  product
            IndexRequest indexRequest = new IndexRequest(ESConstant.PRODUCT_INDEX);
            // 设置id
            indexRequest.id(skuESModel.getSkuId().toString());
            // 设置文档
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(skuESModel);
            indexRequest.source(json, XContentType.JSON);
            // 转换后的数据封装到Bulk中
            bulkRequest.add(indexRequest);
        }
        // 批量向ElasticSearch中保存数据
        BulkResponse bulk = client.bulk(bulkRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        // 获取批量操作是否有错误
        boolean b = bulk.hasFailures();
        /*for (BulkItemResponse item : bulk.getItems()) {
            item.get
        }*/
        return !b;
    }
}
