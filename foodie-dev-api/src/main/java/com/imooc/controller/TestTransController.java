package com.imooc.controller;

import com.imooc.service.impl.TestTransServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class TestTransController {
    @Autowired
    private TestTransServiceImpl testTransService;

    @PostMapping("/demo")
    @Transactional(propagation = Propagation.REQUIRED)
    public void demo() {
        testTransService.saveParent();
        try {
            //相当于数据库中的savepoint 捕获异常之后，不影响其他事务操作
            testTransService.saveChildren();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
