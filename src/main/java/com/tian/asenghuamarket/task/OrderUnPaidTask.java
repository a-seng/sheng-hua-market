package com.tian.asenghuamarket.task;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tian.asenghuamarket.Dto.*;
import com.tian.asenghuamarket.common.Constants;
import com.tian.asenghuamarket.common.OrderStatusEnum;
import com.tian.asenghuamarket.mapper.*;
import com.tian.asenghuamarket.redis.RedisCache;
import com.tian.asenghuamarket.service.CouponService;
import com.tian.asenghuamarket.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class OrderUnPaidTask extends Task{

    /**
     * 默认延迟时间30分钟
     */
    private static final long DELAY_TIME= 30 * 60 * 1000;
    private final Logger log = LoggerFactory.getLogger(OrderUnPaidTask.class);
    /**
     * 订单id
     */
    private final Long orderId;

    public OrderUnPaidTask(Long orderId, long delayInMilliseconds) {
        super("OrderUnPaidTask-" + orderId, delayInMilliseconds);
        this.orderId = orderId;
    }

    public OrderUnPaidTask(Long orderId) {
        super("OrderUnPaidTask-" + orderId, DELAY_TIME);
        this.orderId = orderId;
    }


    @Override
    public void run() {
        log.info("系统开始处理延时任务---订单超时未付款--- {}", this.orderId);
        OrderMapper orderMapper = SpringContextUtil.getBean(OrderMapper.class);
        OrderItemMapper orderItemMapper = SpringContextUtil.getBean(OrderItemMapper.class);
        GoodsInfoMapper goodsInfoMapper = SpringContextUtil.getBean(GoodsInfoMapper.class);
        CouponService couponService = SpringContextUtil.getBean(CouponService.class);

        Order order = orderMapper.selectById(orderId);
        if(order == null){
            log.info("系统结束处理延时任务---订单超时未付款--- {}", this.orderId);
            return;
        }

        if(order.getOrderStatus() != OrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()){
            log.info("系统结束处理延时任务---订单超时未付款--- {}", this.orderId);
            return;
        }

        //设置订单为已取消状态
        order.setOrderStatus((int) OrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus());
        UpdateWrapper<Order> update = Wrappers.update();
        update.eq("orderId",order.getOrderId());
        if(orderMapper.update(order,update)<=0){
            throw new RuntimeException("更新数据已失效");
        }

        //商品货品数量增加
        List<OrderItem>orderItems= orderItemMapper.selectBatchIds(Collections.singleton(orderId));
        for (OrderItem orderItem : orderItems) {
            if(orderItem.getSeckillId()!=null){
                Long seckillId=orderItem.getSeckillId();
                SeckillMapper seckillMapper = SpringContextUtil.getBean(SeckillMapper.class);
                RedisCache redisCache = SpringContextUtil.getBean(RedisCache.class);
                Seckill seckill = new Seckill();
                seckill.setSeckillId(seckillId);
                if( seckillMapper.insert(seckill)==0){
                    throw new RuntimeException("秒杀商品货品库存增加失败");
                }
                redisCache.increment(Constants.SECKILL_GOODS_STOCK_KEY + seckillId);
            }else{

                Long goodsId = orderItem.getGoodsId();
                Integer goodsCount = orderItem.getGoodsCount();
                GoodsInfo goodsInfo = goodsInfoMapper.selectById(goodsId);
                goodsInfo.setStockNum(goodsInfo.getStockNum()+orderItem.getGoodsCount());
                UpdateWrapper<GoodsInfo>updateWrapper=Wrappers.update();
                updateWrapper.eq("goodsId",goodsInfo.getGoodsId());
                if(goodsInfoMapper.update(goodsInfo,updateWrapper)==0){
                    throw new RuntimeException("商品货品库存增加失败");
                }
            }
        }

        //返还优惠券
        couponService.releaseCoupon(orderId);
        log.info("系统结束处理延时任务---订单超时未付款--- {}", this.orderId);

    }
}
