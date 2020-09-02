package com.imooc.service;

import com.imooc.pojo.Stu;
import org.springframework.stereotype.Service;

public interface StuService {
    public Stu getStuById(int id);
    public void deleteStuById(int id);
    public void updateStu(Stu stu);
    public void addStu(Stu stu);
}
