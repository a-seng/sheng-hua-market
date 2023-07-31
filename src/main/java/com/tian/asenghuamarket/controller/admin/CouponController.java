package com.tian.asenghuamarket.controller.admin;


import com.tian.asenghuamarket.Dto.Coupon;
import com.tian.asenghuamarket.service.CouponService;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.Result;
import com.tian.asenghuamarket.util.ResultGenerator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("admin")
public class CouponController {

    @Resource
    private CouponService couponService;

    @GetMapping("/coupon")
    public String index (HttpServletRequest request){
        request.setAttribute("path","coupon");
        return "admin/coupon";
    }

    @ResponseBody
    @GetMapping("/coupon/list")
    public Result list(@RequestParam Map<String,Object> params){
        if(StringUtils.isEmpty((CharSequence)params.get("page"))||
            StringUtils.isEmpty((CharSequence) params.get("limit"))){
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageQueryUtil=new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(couponService.getCouponPage(pageQueryUtil));
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/coupon/save")
    public Result save(@RequestBody Coupon coupon){
        return ResultGenerator.genDmlResult(couponService.saveCoupon(coupon));

    }

    /**
     * 更新
     */
    @PostMapping("/coupon/update")
    @ResponseBody
    public Result update(@RequestBody Coupon coupon){
        return ResultGenerator.genDmlResult(couponService.updateCoupon(coupon));
    }

    /**
     * 详情
     */
    @GetMapping("/coupon/{id}")
    @ResponseBody
    public Result Info(@PathVariable("id")Long id){
        Coupon coupon=couponService.getCouponById(id);
        return ResultGenerator.genSuccessResult(coupon);
    }
    /**
     * 删除
     */
    @GetMapping("/coupon/{id}")
    @ResponseBody
    public Result delete(@PathVariable Long id){
        return ResultGenerator.genDmlResult(couponService.deleteCouponById(id));
    }


}
