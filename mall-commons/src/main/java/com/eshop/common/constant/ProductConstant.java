package com.eshop.common.constant;

/**
 * @Author ruomengjiang
 * @Date 2022/6/10
 * @Description : eshop-mall  商品模块常量
 * @Version: 1.0
 */
public class ProductConstant {

    /**
     * 属性枚举类
     */
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");

        private int code;

        private String msg;

        AttrEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 上架状态枚举类
     */
    public enum StatusEnum{
        SPU_NEW(0,"新建"),
        SPU_UP(1,"上架"),
        SPU_DOWN(2,"下架");
        private int code;
        private String msg;
        StatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        public int getCode(){
            return  code;
        }

        public String getMsg() {
            return msg;
        }
    }

}
