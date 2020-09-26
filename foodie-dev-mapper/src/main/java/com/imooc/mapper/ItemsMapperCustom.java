package com.imooc.mapper;

import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopcartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {
    public List<ItemCommentVO> queryItemComments(@Param("paramsMap")Map<String,Object> map);
    public List<SearchItemsVO> searchItems(@Param("paramsMap")Map<String,Object> map);
    public List<SearchItemsVO> searchItemsByThirdCatId(@Param("paramsMap")Map<String,Object> map);
    public List<ShopcartVo> queryItemsBySpecIds(@Param("paramsList")List specIdList);
    public int decreaseItemSpecStock(@Param("specId") String specId,
                                     @Param("pendingCounts") int pendingCounts);
}