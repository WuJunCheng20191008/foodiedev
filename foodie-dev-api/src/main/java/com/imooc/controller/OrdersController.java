package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.service.AddressService;
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
    private AddressService addressService;

    @ApiOperation(value = "用户下单",notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(
            @RequestBody SubmitOrderBo submitOrderBo){
        //1.创建订单
        //2.创建订单之后，移除购物车中已经算的（已提交）的商品
        //3.向支付中心发送当前订单，用于保存支付中西你的订单数据
        System.out.println(submitOrderBo);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户新增地址",notes = "用户新增地址",httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @ApiParam(name = "addressBo",value = "地址业务对象",required = true)
            @RequestBody AddressBo addressBo){
        IMOOCJSONResult checkRes = checkAddress(addressBo);
        if(checkRes.getStatus()!=200){
            return checkRes;
        }
        addressService.addNewUserAddress(addressBo);
        return IMOOCJSONResult.ok();
    }
    private  IMOOCJSONResult checkAddress(AddressBo addressBo){
        String receiver=addressBo.getReceiver();
        if(StringUtils.isBlank(receiver)){
            return IMOOCJSONResult.errorMsg("收件人不能为空");
        }
        if(receiver.length()>12){
            return IMOOCJSONResult.errorMsg("收件人姓名不能太长");
        }
        String mobile=addressBo.getMobile();
        if(StringUtils.isBlank(mobile)){
            return IMOOCJSONResult.errorMsg("收件人手机号不能为空");
        }
        if(mobile.length()!=11){
            return IMOOCJSONResult.errorMsg("收件人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if(!isMobileOk){
            return IMOOCJSONResult.errorMsg("收件人手机号格式不正确");
        }
        String province=addressBo.getProvince();
        String city=addressBo.getCity();
        String district=addressBo.getDistrict();
        String detail=addressBo.getDetail();
        if(StringUtils.isBlank(province)||
                StringUtils.isBlank(city)||
                StringUtils.isBlank(district)||
                StringUtils.isBlank(detail)){
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户修改地址",notes = "用户修改地址",httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(
            @ApiParam(name = "addressBo",value = "地址业务对象",required = true)
            @RequestBody AddressBo addressBo){
        if(StringUtils.isBlank(addressBo.getAddressId())){
            IMOOCJSONResult.errorMsg("修改地址错误：addressId不能为空");
        }
        IMOOCJSONResult checkRes = checkAddress(addressBo);
        if(checkRes.getStatus()!=200){
            return checkRes;
        }
        addressService.updateUserAddress(addressBo);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户删除地址",notes = "用户删除地址",httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(addressId)){
            IMOOCJSONResult.errorMsg("");
        }

        addressService.deleteUserAddress(userId,addressId);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户设置默认地址",notes = "用户设置默认地址",httpMethod = "POST")
    @PostMapping("/setDefalut")
    public IMOOCJSONResult setDefalut(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(addressId)){
            IMOOCJSONResult.errorMsg("");
        }

        addressService.updateUserAddressToBeDefault(userId,addressId);
        return IMOOCJSONResult.ok();
    }
}
