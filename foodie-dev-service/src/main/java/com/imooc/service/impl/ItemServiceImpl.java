package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.CommentLevel;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopcartVo;
import com.imooc.service.ItemService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example=new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsImgMapper.selectByExample(example);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example=new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsSpecMapper.selectByExample(example);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example=new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsParamMapper.selectOneByExample(example);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts=getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts=getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts=getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts=goodCounts+normalCounts+badCounts;
        CommentLevelCountsVO countsVO=new CommentLevelCountsVO();
        countsVO.setBadCounts(badCounts);
        countsVO.setGoodCounts(goodCounts);
        countsVO.setNormalCounts(normalCounts);
        countsVO.setTotalCounts(totalCounts);
        return countsVO;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId,Integer level){
        ItemsComments condition=new ItemsComments();
        condition.setItemId(itemId);
        if(level!=null){
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level,
                                                  Integer page,Integer pageSize) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("itemId",itemId);
        map.put("level",level);
        /*
        * page:第几页
        * pageSize:每页显示条数
        * */
        //统一拦截sql，为其提供分页功能
        PageHelper.startPage(page,pageSize);
        List<ItemCommentVO> list = itemsMapperCustom.queryItemComments(map);
        // 使用脱敏工具类 显示昵称
        for(ItemCommentVO itemCommentVO:list){
            itemCommentVO.setNickname(DesensitizationUtil.commonDisplay(itemCommentVO.getNickname()));
        }
        return setterPagedGrid(list,page);
    }
    public PagedGridResult setterPagedGrid(List<?> list,Integer page){
        //分页数据封装到PagedGridResult.java 传给前端
        PageInfo<?> pageList=new PageInfo<>(list);
        PagedGridResult grid=new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("keywords",keywords);
        map.put("sort",sort);
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> list = itemsMapperCustom.searchItems(map);
        return setterPagedGrid(list,page);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItemsByThirdCatId(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("catId",catId);
        map.put("sort",sort);
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> list = itemsMapperCustom.searchItemsByThirdCatId(map);
        return setterPagedGrid(list,page);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVo> queryItemsBySpecIds(String specIds) {
        String ids[]=specIds.split(",");
        List<String> specIdList=new ArrayList<String>();
        //把数据中的数据全部添加到list中
        Collections.addAll(specIdList,ids);
        return itemsMapperCustom.queryItemsBySpecIds(specIdList);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg=new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result!=null?result.getUrl():"";
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public  void decreaseItemSpecStock(String specId, Integer buyCounts) {
        /*
        synchronized 不适用原因：
        1. 效率低
        2. 做集群，不起作用
        锁表：不适合原因：
        1. 万一出了故障，数据库性能非常低
         */
        // 采用分布式锁 zookeeper，redis
        // 目前使用乐观锁，直接使用sql语句进行更新
        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        //如果数据库操作成功，返回更新数量，否则抛出异常
        if(result!=1){
            throw new RuntimeException("订单创建失败，原因：库存不足");
        }
    }
}
