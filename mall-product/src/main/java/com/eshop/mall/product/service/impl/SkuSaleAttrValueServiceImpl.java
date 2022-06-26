package com.eshop.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eshop.common.utils.PageUtils;
import com.eshop.common.utils.Query;
import com.eshop.mall.product.dao.SkuSaleAttrValueDao;
import com.eshop.mall.product.entity.SkuSaleAttrValueEntity;
import com.eshop.mall.product.service.SkuSaleAttrValueService;
import com.eshop.mall.product.vo.SkuItemSaleAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *
     * @param spuId
     * @return
     */
    @Override
    public List<SkuItemSaleAttrVo> getSkuSaleAttrValueBySpuId(Long spuId) {
        List<SkuItemSaleAttrVo>  attrsVo = skuSaleAttrValueDao.getSkuSaleAttrValueBySpuId(spuId);
        return attrsVo;
    }

}