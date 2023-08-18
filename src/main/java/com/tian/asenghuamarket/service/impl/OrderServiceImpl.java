package com.tian.asenghuamarket.service.impl;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tian.asenghuamarket.Dto.*;
import com.tian.asenghuamarket.common.*;
import com.tian.asenghuamarket.config.ProjectConfig;
import com.tian.asenghuamarket.controller.vo.*;
import com.tian.asenghuamarket.exception.AsengHuaMarketException;
import com.tian.asenghuamarket.mapper.*;
import com.tian.asenghuamarket.service.OrderService;
import com.tian.asenghuamarket.task.OrderUnPaidTask;
import com.tian.asenghuamarket.task.TaskService;
import com.tian.asenghuamarket.util.BeanUtil;
import com.tian.asenghuamarket.util.NumberUtil;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ShoppingCartItemMapper shoppingCartItemMapper;
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private UserCouponRecordMapper userCouponRecordMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private SeckillSuccessMapper seckillSuccessMapper;
    @Autowired
    private TaskService taskService;

    @Override
    public PageResult<Order> getNewBeeMallOrdersPage(PageQueryUtil pageUtil) {
        return null;
    }

    @Override
    public String updateOrderInfo(Order order) {
        Order temp = orderMapper.selectById(order.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if(temp != null && temp.getOrderStatus()>=0 && temp.getOrderStatus()<3){
            temp.setTotalPrice(order.getTotalPrice());
            temp.setUserAddress(order.getUserAddress());
            temp.setUpdateTime(new Date());
            if(orderMapper.updateById(temp)>0){
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public boolean updateByPrimaryKeySelective(Order order) {
        return orderMapper.updateById(order)>0;
    }

    @Override
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<Order> orders=null;
        StringBuilder errorOrderNos = new StringBuilder();
        for (Long id : ids) {
            orders.add(orderMapper.selectById(id));
        }
        if(!CollectionUtils.isEmpty(orders)){
            for (Order order : orders) {
                if(order.getIsDelete()== 1){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                    continue;
                }
                if(order.getOrderStatus()!=1){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                }
            }
            if(StringUtils.isEmpty(errorOrderNos.toString())){
                //订单状态正常 可以执行配货完成操作，修改订单状态和更新时间
                if(orderMapper.ch){
                    return ServiceResultEnum.SUCCESS.getResult();
                }else{
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            }else{
                //订单此时不可执行出库操作
                if(errorOrderNos.length()>0 && errorOrderNos.length()<100){
                    return errorOrderNos+ "订单的状态不是支付成功无法执行出库操作";
                }else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<Order> orders = orderMapper.selectBatchIds(Arrays.asList(ids));
        StringBuilder errorOrderNos = new StringBuilder();
        if(!CollectionUtils.isEmpty(orders)){
            for (Order order : orders) {
                if(order.getIsDelete()==1){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                    continue;
                }
                if(order.getOrderStatus()!=1 && order.getOrderStatus()!=2){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                }
            }
            if(StringUtils.isEmpty(errorOrderNos.toString())){
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if(orderMapper.){
                    return ServiceResultEnum.SUCCESS.getResult();
                }else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            }else {
                //订单此时不可执行出库操作
                if(errorOrderNos.length()>0 && errorOrderNos.length()<100){
                    return errorOrderNos+"订单的状态不是支付成功或配货完成无法执行出库操作";
                }else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 ，返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<Order> orders = orderMapper.selectBatchIds(Arrays.asList(ids));
        StringBuilder errorOrderNos = new StringBuilder();
        if(!CollectionUtils.isEmpty(orders)){
            for (Order order : orders) {
                //isDeleted = 1 一定为已关闭订单
                if(order.getIsDelete()==1){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if(order.getOrderStatus() == 4 || order.getOrderStatus()<0){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                }
            }
            if(StringUtils.isEmpty(errorOrderNos.toString())){
                //订单状态正常， 可以执行关闭操作 修改订单状态和更新时间
                if(orderMapper.closeOrder(Arrays.asList(ids), OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus())){
                    return ServiceResultEnum.SUCCESS.getResult();
                }else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            }else{
                //订单此时不可执行关闭操作
                if(errorOrderNos.length()>0 && errorOrderNos.length()<100){
                    return errorOrderNos+"订单不能执行关闭操作";
                }else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(UserVO user, Long couponUserId, List<ShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(ShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(ShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<GoodsInfo> goods = goodsInfoMapper.selectBatchIds(goodsIds);
        //检查是否包含已下架商品
        List<GoodsInfo>goodsListNotSelling=goods.stream().filter(goodsTemp->goodsTemp.getGoodsSellStatus()!= Constants.SELL_STATUS_UP).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(goodsListNotSelling)){
            //goodsListNotSelling 对象非空则表示有下架商品
            AsengHuaMarketException.fail((goodsListNotSelling.get(0).getGoodsName())+"已经下架，无法生成订单";
        }
        Map<Long,GoodsInfo> goodsMap= goods.stream().collect(Collectors.toMap(GoodsInfo::getGoodsId,
                Function.identity(),(entity1,entity2)->entity1));
        //判断商品库存
        for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查处的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if(!shoppingCartItemVO.containsKey(shoppingCartItemVO.getGoodsId()))){
                AsengHuaMarketException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if(shoppingCartItemVO.getGoodsCount()>goodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()){
                AsengHuaMarketException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR).getResult();
            }
        }
        if(CollectionUtils.isEmpty(itemIdList)|| CollectionUtils.isEmpty(goodsIds)|| CollectionUtils.isEmpty(goods)){
            AsengHuaMarketException.fail(ServiceResultEnum.ORDER_GENERATE_ERROR.getResult());
        }
        if(shoppingCartItemMapper.deleteBatchIds(itemIdList)<=0){
            AsengHuaMarketException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
        int updateStockNumResult = goodsInfoMapper.update(stockNumDTOS);
        if(updateStockNumResult<1){
            AsengHuaMarketException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
        }
        //生成订单号
        String orderNo = NumberUtil.genOrderNo();
        int priceTotal=0;
        //保存订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(user.getUserId());
        order.setUserAddress(user.getAddress());
        //总价
        for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            priceTotal+=shoppingCartItemVO.getGoodsCount()*shoppingCartItemVO.getSellingPrice();
        }
        //如果使用了优惠券
        if(couponUserId != null){
            UserCouponRecord userCouponRecord = userCouponRecordMapper.selectById(couponUserId);
            Long userId = userCouponRecord.getUserId();
            if(!Objects.equals(userId,user.getUserId())){
                AsengHuaMarketException.fail("优惠券所属用户与当前用户不一致!");
            }
            Long couponId = userCouponRecord.getCouponId();
            Coupon coupon = couponMapper.selectById(couponId);
            priceTotal-=coupon.getDiscoutn();
        }
        if(priceTotal<1){
            AsengHuaMarketException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
        }
        order.setTotalPrice(priceTotal);
        String extraInfo = "asenghuamarket支付宝沙箱支付";
        order.setExtraInfo(extraInfo);
        //生成订单项并保存订单项记录
        if(orderMapper.insert(order)<=0){
            AsengHuaMarketException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        //如果使用了优惠券，则更新优惠券状态
        if(couponUserId!=null){
            UserCouponRecord userCouponRecord = new UserCouponRecord();
            userCouponRecord.setCouponId(couponUserId);
            userCouponRecord.setOrderId(order.getOrderId());
            userCouponRecord.setUseStatus((int) 1);
            userCouponRecord.setUsedTime(new Date());
            userCouponRecord.setUpdateTime(new Date());
            userCouponRecordMapper.updateById(userCouponRecord);
        }
        //生成所有的订单项快照，并保存至数据库
        List<OrderItem> orderItems = new ArrayList<>();
        for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            OrderItem orderItem = new OrderItem();
            //使用BeanUtil工具类将shoppingCartItemVO中的属性复制到orderItem对象中
            BeanUtil.copyProperties(shoppingCartItemVO,orderItem);
            //OrderMapper文件insert()方法中使用了userGeneratedKeys因此orderId可以获取到
            orderItem.setOrderId(order.getOrderId());
            orderItems.add(orderItem);
        }
        //保存至数据库
        if(orderItems == null){
            AsengHuaMarketException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        for (OrderItem orderItem : orderItems) {
            orderItemMapper.insert(orderItem);
        }
        //订单字符超期任务，超过300秒自动取消订单
        taskService.addTask(new OrderUnPaidTask(order.getOrderId(),ProjectConfig.getOrderUnpaidOverTime() * 1000));
        // 所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
        return orderNo;


    }

    @Override
    public String seckillSaveOrder(Long seckillSuccessId, Long userId) {
        SeckillSuccess seckillSuccess = seckillSuccessMapper.selectById(seckillSuccessId);
        if(!seckillSuccess.getUserId().equals(userId)){
            throw new AsengHuaMarketException("当前登录用户与抢购秒杀商品的用户不匹配");
        }
        Long seckillId = seckillSuccess.getSeckillId();
        Seckill seckill = seckillMapper.selectById(seckillId);
        Long goodsId = seckill.getGoodsId();
        GoodsInfo goodsInfo = goodsInfoMapper.selectById(goodsId);
        //生成订单号
        String orderNo = NumberUtil.getOrderNo();
        //保存订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setTotalPrice(seckill.getSeckillPrice());
        order.setUserId(userId);
        order.setUserAddress("秒杀测试地址");
        order.setOrderStatus(OrderStatusEnum.ORDER_PAID.getOrderStatus());
        order.setPayStatus(PayStatusEnum.PAY_SUCCESS.getPayStatus());
        order.setPayType(PayTypeEnum.WEIXIN_PAY.getPayType());
        order.setPayTime(new Date());
        String extraInfo="";
        order.setExtraInfo(extraInfo);
        if(orderMapper.insert(order)<=0){
            throw new AsengHuaMarketException("生成订单内部异常");
        }
        //保存订单品项
        OrderItem orderItem = new OrderItem();
        Long orderId = order.getOrderId();
        orderItem.setOrderId(orderId);
        orderItem.setSeckillId(seckillId);
        orderItem.setGoodsId(goodsInfo.getGoodsId());
        orderItem.setGoodsCoverImg(goodsInfo.getGoodsCoverImg());
        orderItem.setGoodsName(goodsInfo.getGoodsName());
        orderItem.setGoodsCount(1);
        orderItem.setSellingPrice(seckill.getSeckillPrice());
        if(orderItemMapper.insert(orderItem)<=0){
            throw new AsengHuaMarketException("生成订单内部异常");
        }
        //订单支付超期任务
        taskService.addTask(new OrderUnPaidTask(order.getOrderId(),30*1000);

        return orderNo;
    }

    @Override
    public OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("orderNo",orderNo);
        Order order = orderMapper.selectOne(wrapper);
        if(order == null){
            AsengHuaMarketException.fail(ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult());
        }
        //验证是否当前userId下的订单，否则报错
        if(!userId.equals(order.getOrderId())){
            AsengHuaMarketException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId",order.getOrderId());
        List<OrderItem> orderItems = orderItemMapper.selectByMap(map);
        //获取订单项数据
        if(CollectionUtils.isEmpty(orderItems)){
            AsengHuaMarketException.fail(ServiceResultEnum.ORDER_ITEM_NOT_EXIST_ERROR.getResult());
        }
        List<OrderItemVO> orderItemVOS = BeanUtil.copyList(orderItems, OrderItemVO.class);
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtil.copyProperties(order,orderDetailVO);
        orderDetailVO.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(orderDetailVO.getOrderStatus()).getName());
        orderDetailVO.setPayStatusString(PayTypeEnum.getPayTypeEnumByType(orderDetailVO.getPayType()).getName());
        orderDetailVO.setNewBeeMallOrderItemVOS(orderItemVOS);
        return orderDetailVO;

    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("orderNo",orderNo);
        return orderMapper.selectOne(wrapper);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        Integer total = orderMapper.selectCount(new QueryWrapper<>());
        List<OrderListVO> orderListVOS = new ArrayList<>();
        if(total>0){
            List<Order> orders = orderMapper.selectList(new QueryWrapper<>());
            //数据转换，将实体转成VO
            orderListVOS = BeanUtil.copyList(orders, OrderListVO.class);
            //设置订单状态中文显示值
            for (OrderListVO orderListVO : orderListVOS) {
                orderListVO.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(orderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = orders.stream().map(Order::getOrderId).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(orderIds)){
                List<OrderItem> orderItems = orderItemMapper.selectBatchIds(orderIds);
                Map<Long, List<OrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(OrderItem::getOrderId));
                for (OrderListVO orderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if(itemByOrderIdMap.containsKey(orderListVO.getOrderId())){
                        List<OrderItem> orderItems1 = itemByOrderIdMap.get(orderListVO.getOrderId());
                        //将OrderItem对象列表转换成OrderItemVO对象列表
                        List<OrderItemVO> orderItemVOS = BeanUtil.copyList(orderItems1, OrderItemVO.class);
                        orderListVO.setOrderItemVOS(orderItemVOS);
                    }
                }
            }
        }

        return new PageResult(orderListVOS,total, pageUtil.getLimit(), pageUtil.getPage());
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("orderNo", orderNo));
        if(order!=null){
            //验证是否是当前userId下的订单，否则报错
            if(!userId.equals(order.getUserId())){
                AsengHuaMarketException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            //订单状态判断
            if(order.getOrderStatus().intValue()==OrderStatusEnum.ORDER_SUCCESS.getOrderStatus()||
                order.getOrderStatus().intValue()==OrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()||
                order.getOrderStatus().intValue()==OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()||
                order.getOrderStatus().intValue()==OrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()){
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            if(orderMapper.){

            }else{
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("orderNo", orderNo));
        if(order!=null){
            //验证是否是当前userId下的订单，否则报错
            if(!userId.equals(order.getUserId())){
                return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
            }
            //订单状态判断，非出库状态下不进行修改操作
            if(order.getOrderStatus().intValue()!=OrderStatusEnum.ORDER_EXPRESS.getOrderStatus()){
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            order.setOrderStatus(OrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            order.setUpdateTime(new Date());
            if(orderMapper.updateById(order)>0){
                return ServiceResultEnum.SUCCESS.getResult();
            }else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }

        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("orderNo", orderNo));
        if(order==null){
            return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
        }
        //订单状态判断，废待支付状态下不进行修改操作
        if(order.getOrderStatus().intValue()!=OrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()){
            return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
        }
        order.setOrderStatus(OrderStatusEnum.ORDER_PAID.getOrderStatus());
        order.setPayType(payType);
        order.setPayTime(new Date());
        order.setUpdateTime(new Date());
        if(orderMapper.updateById(order)<=0){
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        taskService.removeTask(new OrderUnPaidTask(order.getOrderId()));
        return ServiceResultEnum.SUCCESS.getResult();

    }

    @Override
    public List<OrderItemVO> getOrderItems(Long id) {
        Order order = orderMapper.selectById(id);
        if(order!=null){
            List<OrderItem> orderItems = orderItemMapper.selectList(new QueryWrapper<OrderItem>().eq("orderId", order.getOrderId()));
            //获取订单项数据
            if(!CollectionUtils.isEmpty(orderItems)){
                return BeanUtil.copyList(orderItems,OrderItemVO.class)
            }


        }
        return null;
    }
}
