package com.imooc.service;

import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.pojo.vo.OrderVO;

public interface OrderService {
    /**
     * 用于创建订单相关信息
     * @param submitOrderBo
     */
    public OrderVO createOrder(SubmitOrderBo submitOrderBo);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId,Integer orderStatus);

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder();
}
