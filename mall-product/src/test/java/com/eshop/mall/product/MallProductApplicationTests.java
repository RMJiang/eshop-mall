package com.eshop.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eshop.mall.product.entity.BrandEntity;
import com.eshop.mall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = MallProductApplication.class)
class MallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {
        BrandEntity entity = new BrandEntity();
        entity.setName("小米2");
        brandService.save(entity);
    }

    @Test
    void selectAll() {
        List<BrandEntity> list = brandService.list();
        for (BrandEntity entity : list) {
            System.out.println(entity);
        }
    }

    @Test
    void selectById() {
        List<BrandEntity> list = brandService
                .list(new QueryWrapper<BrandEntity>().eq("brand_id",2));
        for (BrandEntity entity : list) {
            System.out.println(entity);
        }
    }

}
