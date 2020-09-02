package com.imooc.controller;

import com.imooc.service.StuService;
import com.imooc.pojo.Stu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StuController {
    @Autowired
    private StuService stuService;
    @GetMapping("/getStuById")
    public Object getStuById(int id){
        return stuService.getStuById(id);
    }
    @PostMapping("/addStu")
    public Object addStu(){
        Stu stu=new Stu();
        stu.setAge(1);
        stu.setName("1");
        stuService.addStu(stu);
        return "addStu ok";
    }
    @PostMapping("/updateStu")
    public Object updateStu(){
        Stu stu=new Stu();
        stu.setId(1);
        stu.setName("updateDemon");
        stu.setAge(23);
        stuService.updateStu(stu);
        return "updateDemon ok";
    }
    @PostMapping("/deleteStu")
    public Object deleteStu(){
        stuService.deleteStuById(1);
        return  "deleteStu ok";
    }
}
