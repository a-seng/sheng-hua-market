package com.tian.asenghuamarket.service;

import com.tian.asenghuamarket.Dto.Order;
import com.tian.asenghuamarket.controller.vo.OrderDetailVO;
import com.tian.asenghuamarket.controller.vo.OrderItemVO;
import com.tian.asenghuamarket.controller.vo.ShoppingCartItemVO;
import com.tian.asenghuamarket.controller.vo.UserVO;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;

import java.util.List;

public interface OrderService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult<Order> getNewBeeMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param order
     * @return
     */
    String updateOrderInfo(Order order);

    /**
     * 根据主键修改订单信息
     *
     * @param order
     * @return
     */
    boolean updateByPrimaryKeySelective(Order order);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param couponUserId
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(UserVO user, Long couponUserId, List<ShoppingCartItemVO> myShoppingCartItems);

    /**
     * 生成秒杀订单
     *
     * @param seckillSuccessId
     * @param userId
     * @return
     */
    String seckillSaveOrder(Long seckillSuccessId, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    Order getOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<OrderItemVO> getOrderItems(Long id);
}
