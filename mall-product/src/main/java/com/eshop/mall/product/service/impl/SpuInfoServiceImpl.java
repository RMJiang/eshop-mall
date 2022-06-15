package com.eshop.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eshop.common.constant.ProductConstant;
import com.eshop.common.dto.MemberPrice;
import com.eshop.common.dto.SkuHasStockDto;
import com.eshop.common.dto.SkuReductionDTO;
import com.eshop.common.dto.SpuBoundsDTO;
import com.eshop.common.dto.es.SkuESModel;
import com.eshop.common.utils.PageUtils;
import com.eshop.common.utils.Query;
import com.eshop.common.utils.R;
import com.eshop.mall.product.dao.SpuInfoDao;
import com.eshop.mall.product.entity.*;
import com.eshop.mall.product.fegin.CouponFeignService;
import com.eshop.mall.product.fegin.SearchFeginService;
import com.eshop.mall.product.fegin.WareSkuFeginService;
import com.eshop.mall.product.service.*;
import com.eshop.mall.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    AttrService attrService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BrandService brandService;

    @Autowired
    WareSkuFeginService wareSkuFeginService;

    @Autowired
    SearchFeginService searchFeginService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存商品发布信息
     * @param spuInfoVO
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuInfoVO spuInfoVO) {
        //1.保存spu的基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfoVO,spuInfoEntity);
        //创建时间和更新时间需要手动来同步
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        //spuInfoEntity.setPublishStatus(0);//默认设置为新建状态
        this.save(spuInfoEntity);

        //2.保存spu的详细信息 pms_spu_info_desc
        List<String> decripts = spuInfoVO.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuInfoEntity.getId());
        descEntity.setDecript(String.join(",",decripts));
        spuInfoDescService.save(descEntity);

        //3.保存图集信息 pms_spu_images
        List<String> images = spuInfoVO.getImages();
        List<SpuImagesEntity> imageEntities = images.stream().map((item) -> {
            SpuImagesEntity entity = new SpuImagesEntity();
            entity.setSpuId(spuInfoEntity.getId());
            entity.setImgUrl(item);
            return entity;
        }).collect(Collectors.toList());
        spuImagesService.saveBatch(imageEntities);

        //4.保存规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuInfoVO.getBaseAttrs();
        //对规格数据进行对应的处理
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map((attr) -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setSpuId(spuInfoEntity.getId());//关联商品编号
            valueEntity.setAttrId(attr.getAttrId());
            valueEntity.setAttrValue(attr.getAttrValues());
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(attrEntity.getAttrName());
            valueEntity.setQuickShow(attr.getShowDesc());
            return valueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(productAttrValueEntities);

        //5.保存当前spu对应的sku信息
        //5.1 保存sku基本信息 pms_sku_info
        List<Skus> skus = spuInfoVO.getSkus();
        if(skus!=null && skus.size()>0){
            skus.forEach((item)->{
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSaleCount(0L);
                List<Images> images1 = item.getImages();
                String defaultImages = "";
                for (Images images2 : images1) {
                    if (images2.getDefaultImg() == 1){
                        defaultImages = images2.getImgUrl();
                    }
                }
                skuInfoEntity.setSkuDefaultImg(defaultImages);
                skuInfoService.save(skuInfoEntity);
                //5.2 保存sku图片信息 pms_sku_image
                List<SkuImagesEntity> skuImagesEntities = images1.stream().map(img -> {
                    SkuImagesEntity entity = new SkuImagesEntity();
                    entity.setSkuId(skuInfoEntity.getSkuId());
                    entity.setImgUrl(img.getImgUrl());
                    entity.setDefaultImg(img.getDefaultImg());
                    return entity;
                }).filter(img->{
                    return img.getDefaultImg() ==1; // 为空图片不需要保存
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntities);
                //5.3 保存满减信息, 折扣, 会员价
                // mall-sms: sms_sku_ladder sms_full_reduction sms_member_price
                SkuReductionDTO dto = new SkuReductionDTO();
                BeanUtils.copyProperties(item,dto);
                dto.setSkuId(skuInfoEntity.getSkuId());
                //设置会员价
                if (item.getMemberPrice()!=null && item.getMemberPrice().size()>0){
                    List<MemberPrice> list = item.getMemberPrice().stream().map(memberPrice -> {
                        MemberPrice mDto = new MemberPrice();
                        BeanUtils.copyProperties(memberPrice, mDto);
                        return mDto;
                    }).collect(Collectors.toList());
                    dto.setMemberPrice(list);
                }
                R r = couponFeignService.saveFullReductionInfo(dto);
                if(r.getCode()!=0){
                    log.error("调用coupon满减信息, 折扣, 会员价操作失败");
                }
                //5.4 保存sku销售属性信息 pms_sku_sale_attr_value
                List<Attr> attrs = item.getAttr();
                List<SkuSaleAttrValueEntity> saleAttrValueEntities = attrs.stream().map(sale -> {
                    SkuSaleAttrValueEntity entity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(sale,entity);
                    entity.setSkuId(skuInfoEntity.getSkuId());
                    return entity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(saleAttrValueEntities);
            });
        }

        //6.保存spu的积分信息 mall-sms: sms_spu_bounds
        Bounds bounds = spuInfoVO.getBounds();
        SpuBoundsDTO spuBoundsDTO = new SpuBoundsDTO();
        BeanUtils.copyProperties(bounds,spuBoundsDTO);
        spuBoundsDTO.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundsDTO);
        if (r.getCode() != 0){
            log.error("调用coupon服务存储积分信息操作失败");
        }
    }

    /**
     * SPU信息检索
     * 分页查询
     * 分类 品牌 状态 关键字查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        // 设置对应的检索条件
        // 1. 关键字查询
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            // 需要添加关键字查询
            wrapper.and((w)->{
                w.eq("id",key)
                        .or().like("spu_name",key)
                        .or().like("spu_description",key);
            });
        }
        // status
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        // catalogId
        String catalogId = (String) params.get("catalogId");
        if(!StringUtils.isEmpty(catalogId) && !"0".equalsIgnoreCase(catalogId)){
            wrapper.eq("catalog_id",catalogId);
        }
        // brandId
        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        //根据查询到的分页信息，再查询出对应的类别名称和品牌名称
        List<SpuInfoVO> list = page.getRecords().stream().map(spu -> {
            Long catalogId1 = spu.getCatalogId();
            CategoryEntity categoryEntity = categoryService.getById(catalogId1);
            Long brandId1 = spu.getBrandId();
            BrandEntity brandEntity = brandService.getById(brandId1);
            SpuInfoVO vo = new SpuInfoVO();
            BeanUtils.copyProperties(spu, vo);
            vo.setCatalogName(categoryEntity.getName());
            vo.setBrandName(brandEntity.getName());
            return vo;
        }).collect(Collectors.toList());
        IPage<SpuInfoVO> iPage = new Page<SpuInfoVO>();
        iPage.setRecords(list);
        iPage.setPages(page.getPages());
        iPage.setCurrent(page.getCurrent());
        return new PageUtils(iPage);
    }

    /**
     * 实现商品上架--》商品相关数据存储到ElasticSearch中
     * 1.根据SpuID查询出相关的信息 并封装到对应的对象中
     * 2.将封装的数据存储到ElasticSearch中--》调用mall-search的远程接口
     * 3.更新SpuID对应的状态--》上架
     * @param spuId
     */
    @Override
    public void up(Long spuId) {
        // 1.根据spuId查询相关的信息 封装到SkuESModel对象中
        List<SkuESModel> skuEs = new ArrayList<>();
        // 根据spuID找到对应的SKU信息
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);

        // 对应的规格参数  根据spuId来查询规格参数信息
        List<SkuESModel.Attrs> attrsModel = getAttrsModel(spuId);
        // 需要根据所有的skuId获取对应的库存信息---》远程调用
        List<Long> skuIds = skus.stream().map(sku -> {
            return sku.getSkuId();
        }).collect(Collectors.toList());
        Map<Long, Boolean> skusHasStockMap = getSkusHasStock(skuIds);
        // 2.远程调用mall-search的服务，将SukESModel中的数据存储到ES中
        List<SkuESModel> skuESModels = skus.stream().map(item -> {
            SkuESModel model = new SkuESModel();
            // 先实现属性的复制
            BeanUtils.copyProperties(item,model);
            model.setSubTitle(item.getSkuTitle());
            model.setSkuPrice(item.getPrice());

            // hasStock 是否有库存 --》 库存系统查询  一次远程调用获取所有的skuId对应的库存信息
            if(skusHasStockMap == null){
                model.setHasStock(true);
            }else{
                model.setHasStock(skusHasStockMap.get(item.getSkuId()));
            }
            // hotScore 热度分 --> 默认给0即可
            model.setHotScore(0l);
            // 品牌和类型的名称
            BrandEntity brand = brandService.getById(item.getBrandId());
            CategoryEntity category = categoryService.getById(item.getCatalogId());
            model.setBrandName(brand.getName());
            model.setBrandImg(brand.getLogo());
            model.setCatalogName(category.getName());
            // 需要存储的规格数据
            model.setAttrs(attrsModel);

            return model;
        }).collect(Collectors.toList());
        // 将SkuESModel中的数据存储到ES中
        R r = searchFeginService.productStatusUp(skuESModels);
        // 3.更新SPUID对应的状态
        // 根据对应的状态更新商品的状态
        log.info("----->ES操作完成：{}" ,r.getCode());
        //System.out.println("-------------->"+r.getCode());
        if(r.getCode() == 0){
            // 远程调用成功  更新商品的状态为 上架
            baseMapper.updateSpuStatusUp(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else{
            // 远程调用失败
        }
    }

    /**
     * 根据skuIds获取对应的库存状态
     * @param skuIds
     * @return
     */
    private Map<Long,Boolean> getSkusHasStock(List<Long> skuIds){
        List<SkuHasStockDto> skusHasStock = null;
        if(skuIds == null && skuIds.size() == 0){
            return null;
        }
        try {
            // 调用远程接口获取对应的信息
            skusHasStock = wareSkuFeginService.getSkusHasStock(skuIds);
            //skusHasStock.stream().collect(Collectors.toMap(item->{return item.getSkuId();},item->{return item.getHasStock();}));
            Map<Long, Boolean> map = skusHasStock.stream()
                    .collect(Collectors.toMap(SkuHasStockDto::getSkuId, item -> item.getHasStock()));
            return map;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据spuId获取对应的规格参数
     * @param spuId
     * @return
     */
    private List<SkuESModel.Attrs> getAttrsModel(Long spuId) {
        // 1. product_attr_value 存储了对应的spu相关的所有的规格参数
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrsForSpuId(spuId);
        // 2. attr  search_type 决定该属性是否支持检索
        List<Long> attrIds = baseAttrs.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        // 查询出所有的可以检索的对应的规格参数编号
        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        // baseAttrs中根据可以检索的数据过滤
        List<SkuESModel.Attrs> attrsModel = baseAttrs.stream().filter(item -> {
            return searchAttrIds.contains(item.getAttrId());
        }).map(item -> {
            SkuESModel.Attrs attr = new SkuESModel.Attrs();
            /*attrs.setAttrId(item.getAttrId());
            attrs.setAttrName(item.getAttrName());
            attrs.setAttrValue(item.getAttrValue());*/
            BeanUtils.copyProperties(item, attr);
            return attr;
        }).collect(Collectors.toList());
        return attrsModel;
    }



}