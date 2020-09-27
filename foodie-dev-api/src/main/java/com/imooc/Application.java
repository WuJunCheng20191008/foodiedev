package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/*
* 启动类
* */
@SpringBootApplication
@EnableTransactionManagement
/* 扫描通用mapper所在的包，让mybatis扫描到*/
@MapperScan(basePackages = "com.imooc.mapper")
/*扫描当前包以及相关组件包*/
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@EnableScheduling //开启定时任务
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
