package com.eshop.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eshop.mall.product.entity.BrandEntity;
import com.eshop.mall.product.service.BrandService;
import com.eshop.mall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = MallProductApplication.class)
class MallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

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

    @Test
    public void testFindCatelogPath(){
        Long[] catelogPath = categoryService.findCatelogPath(1432l);
        for (Long aLong : catelogPath) {
            System.out.println(aLong);
        }
    }

    @Test
    public void testStringRedisTemplate(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("name",""+ UUID.randomUUID());
        System.out.println("redis value "+ops.get("name"));
    }

    @Test
    public void testRedissionClient(){
        System.out.println("redissonClient: "+redissonClient);
    }

}
