package com.tian.asenghuamarket.service;

import com.tian.asenghuamarket.Dto.Coupon;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;

public interface CouponService {
    void releaseCoupon(Long orderId) ;

    PageResult getCouponPage(PageQueryUtil pageQueryUtil);

    boolean saveCoupon(Coupon coupon);

    boolean updateCoupon(Coupon coupon);

    boolean deleteCouponById(Long id);

    Coupon getCouponById(Long id);
}
