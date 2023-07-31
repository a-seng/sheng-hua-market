package com.tian.asenghuamarket.service.impl;

import com.tian.asenghuamarket.Dto.Coupon;
import com.tian.asenghuamarket.Dto.UserCouponRecord;
import com.tian.asenghuamarket.mapper.CouponMapper;
import com.tian.asenghuamarket.mapper.GoodsInfoMapper;
import com.tian.asenghuamarket.mapper.UserCouponRecordMapper;
import com.tian.asenghuamarket.service.CouponService;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;

public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponRecordMapper userCouponRecordMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Override
    public void releaseCoupon(Long orderId) {
       UserCouponRecord userCouponRecord = userCouponRecordMapper.selectById(orderId);
       if(userCouponRecord==null){
           return ;
       }
        userCouponRecord.setUseStatus(0);
//       userCouponRecord.setUpdateTime(new Date());
       userCouponRecordMapper.updateById(userCouponRecord);
    }

    @Override
    public PageResult getCouponPage(PageQueryUtil pageQueryUtil) {
        return ;
    }

    @Override
    public boolean saveCoupon(Coupon coupon) {
        return couponMapper.insert(coupon)>0;
    }

    @Override
    public boolean updateCoupon(Coupon coupon) {
        return couponMapper.updateById(coupon)>0;
    }

    @Override
    public boolean deleteCouponById(Long id) {
        return couponMapper.deleteById(id)>0;
    }

    @Override
    public Coupon getCouponById(Long id) {
        return couponMapper.selectById(id);
    }
}
