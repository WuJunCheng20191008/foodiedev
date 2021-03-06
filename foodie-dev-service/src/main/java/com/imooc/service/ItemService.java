package com.imooc.service;

import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.ShopcartVo;
import com.imooc.utils.PagedGridResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品ID查询详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品ID查询图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);
    /**
     * 根据商品ID查询商品规格
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);
    /**
     * 根据商品ID查询商品参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品ID查询商品的评价等级数量
     * @param itemId
     * @return
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品ID查询商品的评价内容（分页）
     * @param itemId
     * @param level
     * @return
     */
    public PagedGridResult queryPagedComments(String itemId, Integer level,
                                              Integer page, Integer pageSize);

    /**
     * 搜索商品列表
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(String keywords, String sort,
                                              Integer page, Integer pageSize);
    /**
     * 搜索商品列表
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItemsByThirdCatId(Integer catId, String sort,
                                              Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新的购物车中商品数据（用于刷新渲染购物车）
     * @param specIds
     * @return
     */
    public List<ShopcartVo> queryItemsBySpecIds(String specIds);

    /**
     * 根据商品规格id获取规格对象
     * @param specId
     * @return
     */
    public ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品id获取商品图片主图url
     * @param itemId
     * @return
     */
    public String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    public void decreaseItemSpecStock(String specId,Integer buyCounts);

}
