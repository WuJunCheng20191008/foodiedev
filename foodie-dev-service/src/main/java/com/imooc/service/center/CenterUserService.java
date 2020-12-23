package com.imooc.service.center;


import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBo;
import com.imooc.pojo.bo.center.CenterUserBo;

public interface CenterUserService {
    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     * @param userId
     * @param centerUserBo
     */
    public Users updateUserInfo(String userId, CenterUserBo centerUserBo);

    /**
     * 修改用户头像
     * @param userId
     * @param faceUrl
     * @return
     */
    public Users updateUserFace(String userId,String faceUrl);
}
