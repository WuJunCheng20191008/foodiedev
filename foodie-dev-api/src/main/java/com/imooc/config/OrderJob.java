package com.imooc.config;

import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/*
* 编写定时任务
* */
@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;
    /*
    * 使用定时任务关闭超期未支付订单，会存在弊端
    *  1.会有时间差，程序不严谨
    *       10.39下单，11：00检查不足一小时，12：00检查，超过一个小时多39分
    *  2.不支持集群
    *       单机可以，可以集群后，就会有多个定时任务
    *       解决方法：只使用一台计算机节点，单独运行所有的定时任务
    * 3.会对数据库全表搜索，非常影响数据库性能
    *       定时任务，仅仅只适用于小型轻量级项目，传统项目
    * 后续课程会涉及到消息队列：MQ->RabbitMQ,Kafka,ZeroMQ
    *       延时任务（队列）
    *       10.12分下单，未付款（10）状态，11：12分检查，如果当前订单还是10，则直接关闭订单
    * */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoCloseOrder(){
        orderService.closeOrder();
        System.out.println("执行定时任务，当前时间为："+
                DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
