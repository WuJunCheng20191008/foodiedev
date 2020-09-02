package com.imooc.controller;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.bo.UserBo;
import com.imooc.service.UserService;
import com.imooc.utils.IMOOCJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passport")//这个注释我忘记过
public class PassportController {
    @Autowired
    private UserService userService;
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
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBo userBo){
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
        userService.createUser(userBo);
        return IMOOCJSONResult.ok();
    }
}
