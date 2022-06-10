package com.eshop.mall.product.controller;

import com.eshop.common.utils.PageUtils;
import com.eshop.common.utils.R;
import com.eshop.mall.product.entity.CategoryEntity;
import com.eshop.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品三级分类
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 23:41:39
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表查询所有
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 三级分类显示
     * @param params
     * @return
     */
    @GetMapping("/listTree")
    //@RequiresPermissions("product:category:list")
    public R listTree(@RequestParam Map<String, Object> params){
        List<CategoryEntity> list = categoryService.queryPageWithTree(params);

        return R.ok().put("data", list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		//categoryService.updateById(category);
        categoryService.updateDetail(category);
        return R.ok();
    }

    /**
     * 批量修改
     */
    @RequestMapping("/updateBatch")
    //@RequiresPermissions("product:category:update")
    public R updateBatch(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }

    /**
     * 批量删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		//categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeCategoryByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
