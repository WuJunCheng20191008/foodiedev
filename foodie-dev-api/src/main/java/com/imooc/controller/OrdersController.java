package com.imooc.controller;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "订单相关",tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController{

    @Autowired
    private OrderService orderService;
    @Autowired
    private RestTemplate restTemplate;
    @ApiOperation(value = "用户下单",notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(
            @RequestBody SubmitOrderBo submitOrderBo,
            HttpServletRequest request, HttpServletResponse response){
        if(submitOrderBo.getPayMethod()!= PayMethod.WEIXIN.type&&
        submitOrderBo.getPayMethod()!=PayMethod.ALIPAY.type){
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        //1.创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBo);
        String orderId=orderVO.getOrderId();

        //2.创建订单之后，移除购物车中已经算的（已提交）的商品
        // TODO 整合redis之后，完善购物车中的已结算商品清楚，并且同步到前端的cookie
        //CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);
        //3.向支付中心发送当前订单，用于保存支付中西你的订单数据
        MerchantOrdersVO merchantOrdersVO=orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);
        //为了方便测试购买，所有支付金额统一改为一分钱
        merchantOrdersVO.setAmount(1);
        //调用第三方接口
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //支付功能由于没有账号密码
        headers.add("imoocUserId","");
        headers.add("password","");
        HttpEntity<MerchantOrdersVO> entity=new HttpEntity<>(merchantOrdersVO,headers);
        ResponseEntity<IMOOCJSONResult> responseEntity =
                restTemplate.postForEntity(paymentUrl, entity, IMOOCJSONResult.class);
        IMOOCJSONResult paymentResult = responseEntity.getBody();
        if(paymentResult.getStatus()!=200){
            return IMOOCJSONResult.errorMsg("支付中心订单创建失败，请联系管理员");
        }

        return IMOOCJSONResult.ok(orderId);
    }
    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId){
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

}
