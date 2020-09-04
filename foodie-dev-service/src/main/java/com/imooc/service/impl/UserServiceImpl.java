package com.imooc.service.impl;

import com.imooc.enums.Sex;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBo;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;
    private final static String USER_FACE="https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%A4%B4%E5%83%8F&hs=0&pn=1&spn=0&di=221430&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=2690587153%2C2643787055&os=1382266830%2C3405735094&simid=4254794901%2C605894912&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=head&bdtype=0&oriquery=%E5%A4%B4%E5%83%8F&objurl=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201706%2F10%2F20170610192627_yhAMN.thumb.700_0.jpeg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B17tpwg2_z%26e3Bv54AzdH3Fks52AzdH3F%3Ft1%3Dbmmmbbncl&gsm=2&islist=&querylist=";
    /*为了生成唯一主键*/
    @Autowired
    private Sid sid;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example example=new Example(Users.class);
        //criteria 条件
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        Users result = usersMapper.selectOneByExample(example);
        return result==null?false:true;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBo userBo) {
        /*产生唯一主键*/
        String userId = sid.nextShort();
        Users user=new Users();
        user.setId(userId);
        user.setUsername(userBo.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //默认用户昵称同用户名
        user.setNickname(userBo.getUsername());
        //默认头像
        user.setFace(USER_FACE);
        //默认生日
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        //默认性别 保密
        user.setSex(Sex.secret.type);
        //设置创造时间 更新时间
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);
        return user;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example example=new Example(Users.class);
        Example.Criteria userCriteria = example.createCriteria();
        userCriteria.andEqualTo("username",username);
        userCriteria.andEqualTo("password",password);
        Users result = usersMapper.selectOneByExample(example);
        return result;
    }
}
