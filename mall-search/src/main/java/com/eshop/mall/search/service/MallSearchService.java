package com.eshop.mall.search.service;

import com.eshop.mall.search.vo.SearchParam;
import com.eshop.mall.search.vo.SearchResult;


/**
 * @Author ruomengjiang
 * @Date 2022/6/19
 * @Description : eshop-mall
 * @Version: 1.0
 */
public interface MallSearchService {
    SearchResult search(SearchParam searchParam);
}
