package com.imooc.mapper;

import com.imooc.pojo.vo.MyOrdersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapperCustom {
    public List<MyOrdersVo> queryMyOrders(@Param("paramsMap") Map<String, Object> map);
}
