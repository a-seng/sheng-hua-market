package com.tian.asenghuamarket.service.impl;

import com.alipay.api.domain.Goods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tian.asenghuamarket.Dto.GoodsInfo;
import com.tian.asenghuamarket.Dto.ShoppingCartItem;
import com.tian.asenghuamarket.common.Constants;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.controller.vo.ShoppingCartItemVO;
import com.tian.asenghuamarket.mapper.GoodsInfoMapper;
import com.tian.asenghuamarket.mapper.ShoppingCartItemMapper;
import com.tian.asenghuamarket.service.ShoppingCartService;
import com.tian.asenghuamarket.util.BeanUtil;
import org.openjdk.nashorn.api.scripting.ScriptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartItemMapper shoppingCartItemMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Override
    public String saveCartItem(ShoppingCartItem shoppingCartItem) {
        QueryWrapper<ShoppingCartItem> shoppingCartItemQueryWrapper = new QueryWrapper<>();
        shoppingCartItemQueryWrapper.eq("user_id",shoppingCartItem.getUserId());
        shoppingCartItemQueryWrapper.eq("goods_id",shoppingCartItem.getGoodsId());
        ShoppingCartItem temp= shoppingCartItemMapper.selectOne(shoppingCartItemQueryWrapper);
        if(temp !=null){
            //已存在则修改该记录
            temp.setGoodsCount(shoppingCartItem.getGoodsCount());
            return updateCartItem(temp);
        }
        GoodsInfo goodsInfo = goodsInfoMapper.selectById(shoppingCartItem.getGoodsId());
        //商品为空
        if(goodsInfo == null){
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        shoppingCartItemQueryWrapper.clear();
        shoppingCartItemQueryWrapper.eq("user_id",shoppingCartItem.getUserId());
        int totelItem=shoppingCartItemMapper.selectCount(shoppingCartItemQueryWrapper);
        //超出单个商品的最大数量
        if(shoppingCartItem.getGoodsCount()> Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER){
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if(totelItem>Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER){
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        //保存记录
        if(shoppingCartItemMapper.insert(shoppingCartItem)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCartItem(ShoppingCartItem shoppingCartItem) {
        ShoppingCartItem shoppingCartItemUpdate = shoppingCartItemMapper.selectById(shoppingCartItem.getCartItemId());
        if(shoppingCartItemUpdate == null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出单个商品的最大数量
        if(shoppingCartItem.getGoodsCount()>Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER){
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //数量相同不会进行修改
        if(shoppingCartItemUpdate.getGoodsCount().equals(shoppingCartItem.getGoodsCount())){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        //userId不同不能修改
        if(!shoppingCartItem.getUserId().equals(shoppingCartItemUpdate.getUserId())){
            return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
        }
        shoppingCartItemUpdate.setGoodsCount(shoppingCartItem.getGoodsCount());
        shoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if(shoppingCartItemMapper.updateById(shoppingCartItemUpdate)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public ShoppingCartItem getCartItemById(Long shoppingCartItemId) {
        return shoppingCartItemMapper.selectById(shoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long shoppingCartItemId, Long userId) {
        ShoppingCartItem shoppingCartItem = shoppingCartItemMapper.selectById(shoppingCartItemId);
        if(shoppingCartItem==null){
            return false;
        }
        //UserId不同不能删除
        if (!userId.equals(shoppingCartItem.getUserId())){
            return false;
        }
        return shoppingCartItemMapper.deleteById(shoppingCartItemId)>0;
    }

    @Override
    public List<ShoppingCartItemVO> getMyShoppingCartItems(Long userId) {
        List<ShoppingCartItemVO>shoppingCartItemVOS=new ArrayList<>();
        QueryWrapper<ShoppingCartItem> shoppingCartItemQueryWrapper = new QueryWrapper<>();
        shoppingCartItemQueryWrapper.eq("is_deleted",0);
        shoppingCartItemQueryWrapper.eq("user_id",userId);
        List<ShoppingCartItem>shoppingCartItems=shoppingCartItemMapper.selectList(shoppingCartItemQueryWrapper);
        if(!CollectionUtils.isEmpty(shoppingCartItems)){
            //查询商品信息并做数据转换
            List<Long> goodsInfoIds = shoppingCartItems.stream().map(ShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<GoodsInfo> goodsInfos = null;
            for (Long goodsInfoId : goodsInfoIds) {
                goodsInfos.add(goodsInfoMapper.selectById(goodsInfoId));
            }
            Map<Long,GoodsInfo> goodsInfoMap=new HashMap<>();
            if(!CollectionUtils.isEmpty(goodsInfos)){
                goodsInfoMap=goodsInfos.stream().collect(Collectors.toMap(GoodsInfo::getGoodsId, Function.identity(),(entity1,entity2)->entity1));
            }
            for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
                ShoppingCartItemVO shoppingCartItemVO = new ShoppingCartItemVO();
                BeanUtil.copyProperties(shoppingCartItem,shoppingCartItemVO);
                if(goodsInfoMap.containsKey(shoppingCartItem.getGoodsId())){
                    GoodsInfo goodsInfoTemp = goodsInfoMap.get(shoppingCartItem.getGoodsId());
                    shoppingCartItemVO.setGoodsCoverImg(goodsInfoTemp.getGoodsCoverImg());
                    String goodsName=goodsInfoTemp.getGoodsName();
                    //字符串过长导致文字超出的问题
                    if(goodsName.length()>28){
                        goodsName=goodsName.substring(0,28)+"...";
                    }
                    shoppingCartItemVO.setGoodsName(goodsName);
                    shoppingCartItemVO.setSellingPrice(goodsInfoTemp.getSellingPrice());
                    shoppingCartItemVOS.add(shoppingCartItemVO);
                }
            }

           }
        return shoppingCartItemVOS;
    }
}
