package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.service.AddressService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Api(value = "地址相关",tags = {"地址相关的api接口"})
@RequestMapping("address")
@RestController
public class AddressController {
    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作：
     * 1.查询用户的所有收货地址
     * 2.新增收获地址
     * 3.删除
     * 4.删除
     * 5.设置默认
     * */
    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "根据用户id获取收货地址列表",notes = "根据用户id获取收货地址列表",httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult list(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("");
        }
        List<UserAddress> list = addressService.queryAll(userId);
        return IMOOCJSONResult.ok(list);
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
}
