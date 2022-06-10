package com.eshop.mall.product.controller;

import com.eshop.common.valid.groups.AddGroupsInterface;
import com.eshop.common.valid.groups.UpdateGroupsInterface;
import com.eshop.common.utils.PageUtils;
import com.eshop.common.utils.R;
import com.eshop.mall.product.entity.BrandEntity;
import com.eshop.mall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 品牌
 *  JSR303校验
 * @author RuomengJiang
 * @email ${email}
 * @date 2022-06-01 23:41:39
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存 带有校验
     */
//    @RequestMapping("/save")
//    //@RequiresPermissions("product:brand:save")
//    public R save(@Valid @RequestBody BrandEntity brand, BindingResult result){
//        if(result.hasErrors()){
//            // 提交的数据经过JSR303校验后有非法的字段
//            Map<String,String> map = new HashMap<>();
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            for (FieldError fieldError : fieldErrors) {
//                // 获取非法数据的 field
//                String field = fieldError.getField();
//                // 获取非法的field的提示信息
//                String defaultMessage = fieldError.getDefaultMessage();
//                map.put(field,defaultMessage);
//            }
//            return R.error(400,"提交的品牌表单数据不合法").put("data",map);
//        }
//		brandService.save(brand);
//
//        return R.ok();
//    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated(AddGroupsInterface.class) @RequestBody BrandEntity brand){
        brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated(UpdateGroupsInterface.class) @RequestBody BrandEntity brand){
		brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

//    @GetMapping("/all")
//    public R queryAllBrand(){
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("华为");
//        return R.ok().put("brands",brandEntity);
//    }

}
