package com.imooc.service.impl;

import com.imooc.mapper.StuMapper;
import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StuServiceImpl implements StuService {
    @Autowired
    private StuMapper stuMapper;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStuById(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }
    //@Transactional 默认为required
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteStuById(int id) {
        stuMapper.deleteByPrimaryKey(id);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateStu(Stu stu) {
        stuMapper.updateByPrimaryKey(stu);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addStu(Stu stu) {
        stuMapper.insert(stu);
    }
}
