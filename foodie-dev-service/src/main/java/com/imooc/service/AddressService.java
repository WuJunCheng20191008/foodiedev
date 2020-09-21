package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;

import java.util.List;

public interface AddressService {
    /**
     * 根据用户id查询用户的收获地址列表
     * @param userId
     * @return
     */
    public List<UserAddress> queryAll(String userId);

    /**
     * 用户新增地址
     * @param addressBo
     */
    public void addNewUserAddress(AddressBo addressBo);
}
