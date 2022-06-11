package com.eshop.mall.product.controller;

import com.eshop.common.utils.PageUtils;
import com.eshop.common.utils.R;
import com.eshop.mall.product.service.AttrService;
import com.eshop.mall.product.vo.AttrResponseVo;
import com.eshop.mall.product.vo.AttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



/**
 * 商品属性
 *
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 23:41:38
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @GetMapping("/{attrType}/list/{catelogId}")
    //@RequiresPermissions("product:attr:list")
    public R baseList(@RequestParam Map<String, Object> params
            ,@PathVariable("catelogId") Long catelogId
            ,@PathVariable("attrType") String attrType){

        PageUtils page = attrService.queryBasePage(params,catelogId,attrType);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		//AttrEntity attr = attrService.getById(attrId);
        AttrResponseVo attrResponseVo =attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrResponseVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVO attrVO){
		//attrService.save(attr);
        attrService.saveAttr(attrVO);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVO attr){
		//attrService.updateById(attr);
        attrService.updateBaseAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     * 如删除基本属性 还需将关联属性组的信息一并删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		//attrService.removeByIds(Arrays.asList(attrIds));
        attrService.removeByIdsDetails(attrIds);
        return R.ok();
    }

}
