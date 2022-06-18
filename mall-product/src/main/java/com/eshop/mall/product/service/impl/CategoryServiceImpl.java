package com.eshop.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eshop.common.utils.PageUtils;
import com.eshop.common.utils.Query;
import com.eshop.mall.product.dao.CategoryDao;
import com.eshop.mall.product.entity.CategoryEntity;
import com.eshop.mall.product.service.CategoryBrandRelationService;
import com.eshop.mall.product.service.CategoryService;
import com.eshop.mall.product.vo.Catalog2VO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查询所有的类别数据，然后将数据封装为树形结构，便于前端使用
     *
     * @param params
     * @return
     */
    @Override
    public List<CategoryEntity> queryPageWithTree(Map<String, Object> params) {
        // 1.查询所有的商品分类信息
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        // 2.将商品分类信息拆解为树形结构【父子关系】
        // 第一步遍历出所有的大类  parent_cid = 0
        List<CategoryEntity> list = categoryEntities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(categoryEntity -> {
                    // 根据大类找到多有的小类  递归的方式实现
                    categoryEntity.setChildrens(getCategoryChildrens(categoryEntity,categoryEntities));
                    return categoryEntity;
                }).sorted((entity1, entity2) -> {
                    return (entity1.getSort() == null ? 0 : entity1.getSort()) - (entity2.getSort() == null ? 0 : entity2.getSort());
                }).collect(Collectors.toList());

        return list;
    }

    /**
     *  查找该大类下的所有的小类  递归查找
     * @param categoryEntity 某个大类
     * @param categoryEntities 所有的类别数据
     * @return
     */
    private List<CategoryEntity> getCategoryChildrens(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> collect = categoryEntities.stream().filter(entity -> {
            // 根据大类找到他的直属的小类
            //return entity.getParentCid() == categoryEntity.getCatId();
            //注意Long数据比较   不在-128-127之间的数据是new Long对象 用equals来代替==
            return entity.getParentCid().equals(categoryEntity.getCatId());
        }).map(entity -> {
            // 根据这个小类递归找到对应的小小类
            entity.setChildrens(getCategoryChildrens(entity, categoryEntities));
            return entity;
        }).sorted((entity1, entity2) -> {
            return (entity1.getSort() == null ? 0 : entity1.getSort()) - (entity2.getSort() == null ? 0 : entity2.getSort());
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 逻辑批量删除操作
     * @param ids
     */
    @Override
    public void removeCategoryByIds(List<Long> ids) {
        // TODO 1.检查这个类别数据是否在其他业务中使用

        //批量逻辑删除操作
        baseMapper.deleteBatchIds(ids);
    }

    /**
     * 根据类别编号查询出对应的父组件
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 根据类别编号查询出对应的父组件
     * 225 22 2
     * @param catelogId
     * @param paths
     * @return
     */
    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity entity = this.getById(catelogId);
        if(entity.getParentCid() != 0){
            findParentPath(entity.getParentCid(),paths);
        }
        return paths;
    }

    /**
     * 更新类别名称 且同步更新级联数据
     * @CacheEvict 在更新数据的同时 同步删除缓存数据
     * @param category
     */
    //@CacheEvict(value = "catagory", key = "'getLeve1Category'")
    @Caching(evict = {
            @CacheEvict(value = "catagory", key = "'getLeve1Category'"),
            @CacheEvict(value = "catagory", key = "'getCatelog2JSON'")
    })
    //@CacheEvict(value = "catagory", allEntries = true) 删除catagory分区下的所有的缓存数据
    @Transactional
    @Override
    public void updateDetail(CategoryEntity category) {
        //更新类别名称
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())){
            //同步更新级联数据
            categoryBrandRelationService.updateCatelogName(category.getCatId(),category.getName());

            //TODO 同步更新其他冗余数据
        }
    }

    /**
     * 查询出所有的商品大类(一级分类)
     * 可以指定对应的缓存名称，起到一个分区作用，一般按照业务来区分
     * @Cacheable({"catagory","product"}) 代表当前方法的返回结果需要缓存
     * 调用该方法时如果缓存中有数据 该方法不会执行
     * 如果缓存中没有数据 则会执行该方法 并把该方法结果放入缓存中
     * 缓存处理：
     *      1.存储在redis中的key时默认生成的
     *      2.默认存储的数据过期时间是-1永久
     *      3.缓存的数据默认使用的jdk序列化机制
     * 改进：
     *      1.生成自定义的key key = "#root.method.name"
     *      2.指定缓存数据存活时间 spring.cache.redis.time-to-live 指定过期时间
     *      3.缓存数据为json数据
     * ,sync = true加本地锁
     * @return
     */
    @Cacheable(value = "catagory", key = "#root.method.name")
    @Override
    public List<CategoryEntity> getLeve1Category() {
        //long start = System.currentTimeMillis();
        //System.out.println("查询了数据库");
        List<CategoryEntity> list = baseMapper.queryLevelCategory();
        //System.out.println("查询消耗的时间"+ (System.currentTimeMillis() - start));
        return list;
    }

    /**
     * 跟进父编号获取对应的子菜单信息
     * @param list
     * @param parentCid
     * @return
     */
    private List<CategoryEntity> queryByParenCid(List<CategoryEntity> list,Long parentCid){
        List<CategoryEntity> collect = list.stream().filter(item -> {
            return item.getParentCid().equals(parentCid);
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 数据库中查询结果
     * 优化 减少数据库查询操作
     * 从数据库中查询出所有的二级和三级分类的数据
     * 并封装为Map<String, Catalog2VO>对象
     * @return
     */
    public Map<String, List<Catalog2VO>> getCatelog2JSONForDb() {
        String keys = "catalogJSON";
        synchronized (this){
            // 先去缓存中查询有没有数据，如果有就返回，否则查询数据库
            // 从Redis中获取分类的信息
            String catalogJSON = stringRedisTemplate.opsForValue().get(keys);
            if(!StringUtils.isEmpty(catalogJSON)){
                // 缓存命中了数据，从缓存中获取信息，然后返回
                Map<String, List<Catalog2VO>> stringListMap = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catalog2VO>>>() {
                });
                return stringListMap;
            }
            //System.out.println("-----------》查询数据库操作");

            // 获取所有的分类数据
            List<CategoryEntity> list = baseMapper.selectList(new QueryWrapper<CategoryEntity>());
            // 获取所有的一级分类的数据
            List<CategoryEntity> leve1Category = this.queryByParenCid(list,0l);
            // 把一级分类的数据转换为Map容器 key就是一级分类的编号， value就是一级分类对应的二级分类的数据
            Map<String, List<Catalog2VO>> map = leve1Category.stream().collect(Collectors.toMap(
                    key -> key.getCatId().toString()
                    , value -> {
                        // 根据一级分类的编号，查询出对应的二级分类的数据
                        List<CategoryEntity> l2Catalogs = this.queryByParenCid(list,value.getCatId());
                        List<Catalog2VO> Catalog2VOs =null;
                        if(l2Catalogs != null){
                            Catalog2VOs = l2Catalogs.stream().map(l2 -> {
                                // 需要把查询出来的二级分类的数据填充到对应的Catelog2VO中
                                Catalog2VO catalog2VO = new Catalog2VO(l2.getParentCid().toString(), null, l2.getCatId().toString(), l2.getName());
                                // 根据二级分类的数据找到对应的三级分类的信息
                                List<CategoryEntity> l3Catelogs = this.queryByParenCid(list,l2.getCatId());
                                if(l3Catelogs != null){
                                    // 获取到的二级分类对应的三级分类的数据
                                    List<Catalog2VO.Catalog3VO> catalog3VOS = l3Catelogs.stream().map(l3 -> {
                                        Catalog2VO.Catalog3VO catalog3VO = new Catalog2VO.Catalog3VO(l3.getParentCid().toString(), l3.getCatId().toString(), l3.getName());
                                        return catalog3VO;
                                    }).collect(Collectors.toList());
                                    // 三级分类关联二级分类
                                    catalog2VO.setCatalog3List(catalog3VOS);
                                }
                                return catalog2VO;
                            }).collect(Collectors.toList());
                        }

                        return Catalog2VOs;
                    }
            ));
            // 缓存命中了数据，则从缓存中获取信息，然后返回
            if(map == null){
                // 数据库中也不存在  防止缓存穿透
                stringRedisTemplate.opsForValue().set(keys,"1",5, TimeUnit.SECONDS);
            }else{
                // 从数据库中查询到的数据，缓存中也存储
                // 防止缓存雪崩
                String json = JSON.toJSONString(map);
                stringRedisTemplate.opsForValue().set("catalogJSON",json,10,TimeUnit.MINUTES);
            }
            return map;
        }
    }

    /**
     * 查询出所有的二级和三级分类的数据
     * 并封装为Map<String, Catalog2VO>对象
     * @return
     */
    @Cacheable(value = "catagory", key = "#root.methodName")
    @Override
    public Map<String, List<Catalog2VO>> getCatelog2JSON() {
        // 获取所有的分类数据
        List<CategoryEntity> list = baseMapper.selectList(new QueryWrapper<CategoryEntity>());
        // 获取所有的一级分类的数据
        List<CategoryEntity> leve1Category = this.queryByParenCid(list,0l);
        // 把一级分类的数据转换为Map容器 key就是一级分类的编号， value就是一级分类对应的二级分类的数据
        Map<String, List<Catalog2VO>> map = leve1Category.stream().collect(Collectors.toMap(
                key -> key.getCatId().toString()
                , value -> {
                    // 根据一级分类的编号，查询出对应的二级分类的数据
                    List<CategoryEntity> l2Catalogs = this.queryByParenCid(list,value.getCatId());
                    List<Catalog2VO> Catalog2VOs =null;
                    if(l2Catalogs != null){
                        Catalog2VOs = l2Catalogs.stream().map(l2 -> {
                            // 需要把查询出来的二级分类的数据填充到对应的Catelog2VO中
                            Catalog2VO catalog2VO = new Catalog2VO(l2.getParentCid().toString(), null, l2.getCatId().toString(), l2.getName());
                            // 根据二级分类的数据找到对应的三级分类的信息
                            List<CategoryEntity> l3Catelogs = this.queryByParenCid(list,l2.getCatId());
                            if(l3Catelogs != null){
                                // 获取到的二级分类对应的三级分类的数据
                                List<Catalog2VO.Catalog3VO> catalog3VOS = l3Catelogs.stream().map(l3 -> {
                                    Catalog2VO.Catalog3VO catalog3VO = new Catalog2VO.Catalog3VO(l3.getParentCid().toString(), l3.getCatId().toString(), l3.getName());
                                    return catalog3VO;
                                }).collect(Collectors.toList());
                                // 三级分类关联二级分类
                                catalog2VO.setCatalog3List(catalog3VOS);
                            }
                            return catalog2VO;
                        }).collect(Collectors.toList());
                    }

                    return Catalog2VOs;
                }
        ));
        return map;
    }

    /**
     * redis实现分布式锁
     * @return
     */
    public Map<String, List<Catalog2VO>> getCatelog2JSONDbWithRedisLock() {
        String keys = "catalogJSON";
        // 加锁 在执行插入操作的同时设置了过期时间
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
        if(lock){
            //System.out.println("获取分布式锁成功。");
            Map<String, List<Catalog2VO>> data = null;
            try {
                // 加锁成功
                data = getDataForDB(keys);
            }finally {
                String srcipts = "if redis.call('get',KEYS[1]) == ARGV[1]  then return redis.call('del',KEYS[1]) else  return 0 end ";
                // 通过Redis的lua脚本实现 查询和删除操作的原子性
                stringRedisTemplate.execute(new DefaultRedisScript<Long>(srcipts,Long.class)
                        ,Arrays.asList("lock"),uuid);
            }
            return data;
        }else{
            // 加锁失败
            // 休眠+重试
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("获取锁失败。");
            return getCatelog2JSONDbWithRedisLock();
        }
    }

    /**
     * 从数据库中查询操作
     * @param keys
     * @return
     */
    private Map<String, List<Catalog2VO>> getDataForDB(String keys){
        // 先去缓存中查询有没有数据，如果有就返回，否则查询数据库
        // 从Redis中获取分类的信息
        String catalogJSON = stringRedisTemplate.opsForValue().get(keys);
        if(!StringUtils.isEmpty(catalogJSON)){
            // 说明缓存命中
            // 表示缓存命中了数据，那么从缓存中获取信息，然后返回
            Map<String, List<Catalog2VO>> stringListMap = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catalog2VO>>>() {
            });
            return stringListMap;
        }
        //System.out.println("-----------》查询数据库操作");

        // 获取所有的分类数据
        List<CategoryEntity> list = baseMapper.selectList(new QueryWrapper<CategoryEntity>());
        // 获取所有的一级分类的数据
        List<CategoryEntity> leve1Category = this.queryByParenCid(list,0l);
        // 把一级分类的数据转换为Map容器 key就是一级分类的编号， value就是一级分类对应的二级分类的数据
        Map<String, List<Catalog2VO>> map = leve1Category.stream().collect(Collectors.toMap(
                key -> key.getCatId().toString()
                , value -> {
                    // 根据一级分类的编号，查询出对应的二级分类的数据
                    List<CategoryEntity> l2Catalogs = this.queryByParenCid(list,value.getCatId());
                    List<Catalog2VO> Catalog2VOs =null;
                    if(l2Catalogs != null){
                        Catalog2VOs = l2Catalogs.stream().map(l2 -> {
                            // 需要把查询出来的二级分类的数据填充到对应的Catelog2VO中
                            Catalog2VO catalog2VO = new Catalog2VO(l2.getParentCid().toString(), null, l2.getCatId().toString(), l2.getName());
                            // 根据二级分类的数据找到对应的三级分类的信息
                            List<CategoryEntity> l3Catelogs = this.queryByParenCid(list,l2.getCatId());
                            if(l3Catelogs != null){
                                // 获取到的二级分类对应的三级分类的数据
                                List<Catalog2VO.Catalog3VO> catalog3VOS = l3Catelogs.stream().map(l3 -> {
                                    Catalog2VO.Catalog3VO catalog3VO = new Catalog2VO.Catalog3VO(l3.getParentCid().toString(), l3.getCatId().toString(), l3.getName());
                                    return catalog3VO;
                                }).collect(Collectors.toList());
                                // 三级分类关联二级分类
                                catalog2VO.setCatalog3List(catalog3VOS);
                            }
                            return catalog2VO;
                        }).collect(Collectors.toList());
                    }

                    return Catalog2VOs;
                }
        ));
        // 从数据库中获取到了对应的信息 然后在缓存中也存储一份信息
        // 表示缓存命中了数据，那么从缓存中获取信息，然后返回
        if(map == null){
            // 那就说明数据库中也不存在  防止缓存穿透
            stringRedisTemplate.opsForValue().set(keys,"1",5, TimeUnit.SECONDS);
        }else{
            // 从数据库中查询到的数据，我们需要给缓存中也存储一份
            // 防止缓存雪崩
            String json = JSON.toJSONString(map);
            stringRedisTemplate.opsForValue().set("catalogJSON",json,10,TimeUnit.MINUTES);
        }
        return map;
    }

    /**
     * redission 实现分布式锁
     * @return
     */
    public Map<String, List<Catalog2VO>> getCatelog2JSONDbWithRedission() {
        String keys = "catalogJSON";
        // 获取分布式锁对象 加锁时 注意锁名称
        RLock lock = redissonClient.getLock("catelog2JSON-lock");
        Map<String, List<Catalog2VO>> data = null;
        try {
            // 加锁成功
            lock.lock();
            data = getDataForDB(keys);
        }finally {
            lock.unlock();
        }
        return data;
    }


    /**
     * 从redis中查询结果 整合数据库中结果
     * 查询出所有的二级和三级分类的数据
     * 并封装为Map<String, Catalog2VO>对象
     * @return
     */
    public Map<String, List<Catalog2VO>> getCatelog2JSONRedis() {
        String key = "catalogJSON";
        // 从Redis中获取分类的信息
        String catalogJSON = stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(catalogJSON)){
            //System.out.println("缓存没有命中.....");
            // 缓存中没有数据，需要从数据库中查询
            Map<String, List<Catalog2VO>> catelog2JSONForDb = getCatelog2JSONDbWithRedission();

            return catelog2JSONForDb;
        }
        //System.out.println("缓存命中了....");
        // 表示缓存命中了数据，那么从缓存中获取信息，然后返回
        Map<String, List<Catalog2VO>> stringListMap = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catalog2VO>>>() {
        });
        return stringListMap;
    }

}