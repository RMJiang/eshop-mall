package com.eshop.mall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.eshop.mall.order.vo.PayVo;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Author ruomengjiang
 * @Date 2022/7/2
 * @Description : eshop-mall
 * 支付宝配置文件
 * @Version: 1.0
 */
@Component
@Data
public class AlipayTemplate {

    // 商户appid
    public static String APPID = "2021000121614159";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCn+DlqyHvluSOj4xR8p+bCd3bXYFkd29Ow9+sb/bqFegnF241wrlx66gRx5JW6TADHuqQkWOMThLz2TlPEHxB8uPBwDp5SWrbbVQ4IgoKmCx04dVw/cwbu9B3dNa22f0qBvxheHWT+5Le2FU3mIB636Ue6MXUARPP2qIlSl1YrGVleV+hGCHDuqaf9CgmegGlrEYSISWj03exmc2lS2Rcnhl+H0Goi2haW1p27+VXzRx1XUJFwG7hWEoPO6gsvKCSSowDpQIzVJ9JD33lNqIOjiw3AcTmROOr8w7ufsesmD4E1eOkBCkcbLFe+yq9vJR30HyaxOiRoDS00PRDhHH8tAgMBAAECggEAKnIGiF5rOKzlPbfXaHzBSmkH5fsZ6fqv7utnRSaD6NDjAVWu00esZP8X6nP6Bh7In7AhZHH1x+vmGFPIxSmYb/wH/jkjduonhRtkNXBNdYofczHtAredi+qyVUEISbK28DyK+tVwvRys891EVx/Mw2q2zm1LBKtn/u+t6+oEQ1V2iL2FZSI/kRaVvq95ZVq4B47j+am7ICzN7xyv3PXDLKBXpjsK3GGVlLgDDF31jjGd4jcHohx42ICiO9xTrEDAzaijXM3o/ijmbJcb/rQng5FKIuna1tWuBu3TXJfGzFFn+kGnDyHISkLh66C7NdDD0v3bu51MJkCVji3k9aFqAQKBgQDcgzai2TUV2oepAd4ZJZG/ug1wGlxEi7m/OIdRQIOMnsxyspYuz05iBoyBdbQv8zI6oiC84lyJkaRqhFaNykV0eb1dD9MmRGju9kKb2DjGdLa7xufNQ0pfSFKFum/LzbVPBfVbDsL4qbEgcDGQX0wobtuUwVAr0M4LXcIi2vMtrQKBgQDDAFIxlRVxgMTxAL+jqgpjehhKZnL/EMzGdlVZE/aIwBq0E+Yjh6nJhR6hdG12z/k+1w6BU1w8lk+6VzJX7i1JjtWz123WK85520bwwNKmwfYxepiHg+spnUnVodAZISoCRWCRcXgtmCeR6fjPEuCtnRrngV+QUW1K8b28tHXHgQKBgAnC0SdSQBRQkRK9H2yzigkXnkKpGA58tOMvCGZjWa9e1CP7ncRCrvCN46hTYn+A32dTB+n0hy4bwVBpvT8Xhg2K2gQFkE4wVwjE49vsLU8zmWckfdxLLz6af/SldQ9rcqwl0l+OfrjtIgcSkE8YuNFNXvDtwbO+0XAVXWOXl44dAoGABsWoFRi8WejC7iu8fA6XtcSzu0G7lKgctHe/lwxeb3lcdAyM3YcEmn8JBUpp0sp+OXR2iH9AoMoHks0cbnlTEU7olQpSQjHbRdgbqpjHuArcZfOAAJTQQe24drbb50ODlfia1fI+3jZ+iU662VYqf10JZijUDMsHJKcok7kgrgECgYEAluYZsQC2/KaXe6f7Ls2PkOoQ1h/SLjsMIFDmI54UBL9ihqQol0Pnk1pfwO/Igc1jxTokA73vx0F8q5Qt3rdbIfptH+LS1QowKp7ikk0YnINsEv6UyrWqPzumt9/KE2PklSYpd0WdqsGmNJd/tLxpECVoLT1eNj7oRxGO5N0czrA=";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://order.eshop.com/payed/notify";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://order.eshop.com/orderPay/returnUrl";
    // 请求网关地址
    public static String URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgPE3Nsnmr+4f/n8Uui89dhmVPoqatnEAtk2j9SClG8s+34tDx/ZNLGrqkJ3WDJ59zsDebCBJLd0JuKuGr5szaHf1cADpEx8DXfW0Ib/U8W8qjiZYFmVyx2HpibqJdI4n0KLC54FDrFv/TeVeC73X5xApuiF7Fig+ke/6ZMQcntce/1MttIphurruIorckB0o6r+Aeu2204zy9JAT/FLSQMza2l1vQrf5ZNJIU2GUjxnv9v24tnfqhyuKTz/8lZ5nOmyYs6is5FHDkoebF+OvqLJz23WDORccH45N4q18OXJ8OvMNZUwFFORvqnOhJJrCS6sO6zrTX6hXmDUUNpFQCwIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";

    public String pay(PayVo payVo){
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(URL,
                APPID,
                RSA_PRIVATE_KEY,
                FORMAT,
                CHARSET,
                ALIPAY_PUBLIC_KEY,
                SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(payVo.getOut_trader_no());
        model.setSubject(payVo.getSubject());
        model.setTotalAmount(payVo.getTotal_amount());
        model.setBody(payVo.getBody());
        model.setTimeoutExpress("5000");
        model.setProductCode("11111");
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(return_url);

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipay_request).getBody();
            return form;
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  null;
    }
}
