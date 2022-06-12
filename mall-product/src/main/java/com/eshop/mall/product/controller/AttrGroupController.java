package com.eshop.mall.product.controller;

import com.eshop.common.utils.PageUtils;
import com.eshop.common.utils.R;
import com.eshop.mall.product.entity.AttrEntity;
import com.eshop.mall.product.entity.AttrGroupEntity;
import com.eshop.mall.product.service.AttrAttrgroupRelationService;
import com.eshop.mall.product.service.AttrGroupService;
import com.eshop.mall.product.service.AttrService;
import com.eshop.mall.product.service.CategoryService;
import com.eshop.mall.product.vo.AttrGroupRelationVO;
import com.eshop.mall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 属性分组
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 23:41:38
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

    @RequestMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> list = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",list);
    }

    /**
     * 列表分类 分页查询
     * 前端提交请求 需要封装对应的分页数据
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        //根据找到的属性组对应的分类id 然后找到对应的【一级，二级，三级】数据
        Long catelogId = attrGroup.getCatelogId();
        Long paths[] = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(paths);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    /**
     * 移除关联信息
     */
    @PostMapping("/attr/relation/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R relationDelete(@RequestBody AttrGroupRelationVO[] vos){
        attrService.deleteRelation(vos);

        return R.ok();
    }

    /**
     * 根据属性id查询出未关联属性信息
     * @param attrgroupId
     * @param params
     * @return
     */
    @GetMapping("/{attrgorupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgorupId") Long attrgroupId
            ,@RequestParam Map<String,Object> params){
        PageUtils pageUtils= attrService.getNoAttrRelation(params,attrgroupId);
        return R.ok().put("page",pageUtils);
    }

    /**
     * 保存
     */
    @PostMapping("/attr/relation")
    //@RequiresPermissions("product:attrgroup:save")
    public R saveBatch(@RequestBody List<AttrGroupRelationVO> vos){
        relationService.saveBatch(vos);

        return R.ok();
    }


    @GetMapping("/{catelogId}/withattr")
    public R getAttrgroupWithAttrs(@PathVariable("catelogId") Long catelogId){
        List<AttrGroupWithAttrsVo> list = attrGroupService.getAttrgroupWithAttrsByCatelogId(catelogId);

        return R.ok().put("data",list);
    }

}
