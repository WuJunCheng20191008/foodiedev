package com.imooc.service.center;


import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBo;

public interface CenterUserService {

    public Users queryUserInfo(String userId);
}
