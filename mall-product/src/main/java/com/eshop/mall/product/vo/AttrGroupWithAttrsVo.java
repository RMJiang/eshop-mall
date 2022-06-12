package com.eshop.mall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.eshop.mall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author ruomengjiang
 * @Date 2022/6/12
 * @Description : eshop-mall
 * @Version: 1.0
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    // 关联对应的属性信息
    private List<AttrEntity> attrs;
}
