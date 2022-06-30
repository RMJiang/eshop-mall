package com.eshop.common.exception;

/**
 * @Author ruomengjiang
 * @Date 2022/6/30
 * @Description : eshop-mall
 * @Version: 1.0
 */
public class NoStockExecption extends RuntimeException{

    private Long skuId;

    public NoStockExecption(Long skuId){
        super("当前商品["+skuId+"]没有库存了");
        this.skuId = skuId;

    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
