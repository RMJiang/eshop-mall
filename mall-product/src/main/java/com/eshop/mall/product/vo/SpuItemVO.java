package com.eshop.mall.product.vo;

import com.eshop.mall.product.entity.SkuImagesEntity;
import com.eshop.mall.product.entity.SkuInfoEntity;
import com.eshop.mall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/24
 * @Description : eshop-mall
 * 商品详情页的数据对象
 * @Version: 1.0
 */
@Data
public class SpuItemVO {

    // 1.sku的基本信息 pms_sku_info
    SkuInfoEntity info;
    // 2.sku的图片信息pms_sku_images
    List<SkuImagesEntity> images;
    // 3.获取spu中的销售属性的组合
    List<SkuItemSaleAttrVo> saleAttrs;
    // 4.获取SPU的介绍
    SpuInfoDescEntity desc;
    // 5.获取SPU的规格参数
    List<SpuItemGroupAttrVo> baseAttrs;
    //6.是否有货
    boolean hasStock = true;
    //7.对应的秒杀服务
    SeckillVO seckillVO;
}
