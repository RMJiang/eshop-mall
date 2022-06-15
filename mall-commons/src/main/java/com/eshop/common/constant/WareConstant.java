package com.eshop.common.constant;

/**
 * @Author ruomengjiang
 * @Date 2022/6/13
 * @Description : eshop-mall  库存模块的常量
 * @Version: 1.0
 */
public class WareConstant {

    /**
     * 采购单状态
     */
    public enum PurchaseStatusEnum{
        CREATED(0,"新建")
        ,ASSIGED(1,"已分配")
        ,RECEIVE(2,"已领取")
        ,FINISH(3,"已完成")
        ,HASERROR(4,"有异常");
        private int code;
        private String msg;
        PurchaseStatusEnum(int code,String msg){
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

    /**
     * 采购需求状态
     */
    public enum PurchaseDetailStatusEnum{
        CREATED(0,"新建")
        ,ASSIGED(1,"已分配")
        ,BUYING(2,"正在采购")
        ,FINISH(3,"已完成")
        ,HASERROR(4,"采购失败");
        private int code;
        private String msg;
        PurchaseDetailStatusEnum(int code,String msg){
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