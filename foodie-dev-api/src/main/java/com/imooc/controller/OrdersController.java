package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.service.AddressService;
import com.imooc.service.OrderService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "订单相关",tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrdersController {

    @Autowired
    private OrderService orderService;
    @ApiOperation(value = "用户下单",notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(
            @RequestBody SubmitOrderBo submitOrderBo){
        if(submitOrderBo.getPayMethod()!= PayMethod.WEIXIN.type&&
        submitOrderBo.getPayMethod()!=PayMethod.ALIPAY.type){
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        //1.创建订单
        orderService.createOrder(submitOrderBo);
        //2.创建订单之后，移除购物车中已经算的（已提交）的商品
        //3.向支付中心发送当前订单，用于保存支付中西你的订单数据
        System.out.println(submitOrderBo);
        return IMOOCJSONResult.ok();
    }

}
