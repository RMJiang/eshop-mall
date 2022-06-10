package com.eshop.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eshop.common.utils.PageUtils;
import com.eshop.common.utils.Query;
import com.eshop.mall.product.dao.CategoryDao;
import com.eshop.mall.product.entity.CategoryEntity;
import com.eshop.mall.product.service.CategoryBrandRelationService;
import com.eshop.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

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
     * @param category
     */
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

}