package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBo;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.util.Password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册登录",tags = {"用于注册登录的相关接口"})/*用于注释该controller*/
@RestController
@RequestMapping("/passport")//这个注释我忘记过
public class PassportController {
    @Autowired
    private UserService userService;
    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")//用于注释该方法
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username){
        //1.判断用户名不能为空
        if(StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        //2.查找注册的用户是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if(isExist){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        //3.请求成功，用户名没有重复
        return IMOOCJSONResult.ok();
    }
    //注册
    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")//用于注释该方法
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBo userBo,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        String username=userBo.getUsername();
        String password=userBo.getPassword();
        String  confirmPwd=userBo.getConfirmPassword();
        //判断用户名 密码不能为空
        if(StringUtils.isBlank(username)||
                StringUtils.isBlank(password)||StringUtils.isBlank(confirmPwd)){
            return  IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if(isExist){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        //密码长度不能少于6位
        if(password.length()<6){
            return IMOOCJSONResult.errorMsg("密码长度不能少于6位");
        }
        //判断两次密码是否一致
        if(!password.equals(confirmPwd)){
            return IMOOCJSONResult.errorMsg("两次密码不一致");
        }
        //实现注册
        Users userResult = userService.createUser(userBo);
        //设置敏感信息为null，返回给前端显示
        userResult = setNullProperty(userResult);
        //设置cookie
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBo userBo,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String username=userBo.getUsername();
        String password=userBo.getPassword();
        //判断用户名和密码必须不为空
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //实现登录
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if(userResult==null){
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }
        //设置敏感信息为null，返回给前端显示
        userResult = setNullProperty(userResult);
        //设置cookie 最后一个参数设置加密
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);
        return IMOOCJSONResult.ok(userResult);
    }
    private Users setNullProperty(Users userResult){
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }
}
