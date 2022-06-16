package com.eshop.mall.product.web;

import com.eshop.mall.product.entity.CategoryEntity;
import com.eshop.mall.product.service.CategoryService;
import com.eshop.mall.product.vo.Catalog2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Author ruomengjiang
 * @Date 2022/6/15
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/","/home","/index","/index.html","/home.html"})
    public String index(Model model){
        List<CategoryEntity> list = categoryService.getLeve1Category();
        model.addAttribute("categorys",list);
        return "index";
    }

    @ResponseBody
    @RequestMapping("/index/catalog.json")
    public Map<String, List<Catalog2VO>> getCatalog2JSON(){
        Map<String, List<Catalog2VO>> map = categoryService.getCatelog2JSON();
        return map;
    }

    @ResponseBody
    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

}
