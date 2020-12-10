package com.imooc.controller;

import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class BaseController {

    public static final String FOODIE_SHOPCART="shopcart";
    public static final Integer COMMENT_PAGE_SIZE=10;
    public static final Integer PAGE_SIZE=20;
    //微信支付成功 -> 支付中心 -> 天天吃货平台 回调通知的url
    String payReturnUrl="http://localhost:8088/orders/notifyMerchantOrderPaid";
    // 支付中心的调用地址
    String paymentUrl="http://payment.t.mukewang.com/foodie-payment/createMerchantOrder/createMerchantOrder";

    //用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION = "D:" +
                                            File.separator+"IdeaProjectsForCsdn" +
                                            File.separator+"images" +
                                            File.separator+"foodiedev" +
                                            File.separator+"faces";

}
