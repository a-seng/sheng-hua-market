package com.tian.asenghuamarket.controller.admin;


import com.tian.asenghuamarket.Dto.Carousel;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.service.CarouselService;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.Result;
import com.tian.asenghuamarket.util.ResultGenerator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class CarouselController {
    @Resource
    CarouselService carouselService;
    @GetMapping("/carousels")
    public String carouselPage(HttpServletRequest request){
        request.setAttribute("path","carousel");
        return "admin/carousel";
    }

    /**
     * 列表
     */
    @GetMapping("/carousels/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object>params){
        if(StringUtils.isEmpty((CharSequence) params.get("page"))||
                StringUtils.isEmpty((CharSequence) params.get("limit"))){
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageQueryUtil=new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(carouselService.getCarouselPage(pageQueryUtil));
    }

    /**
     * 添加
     */
    @PostMapping("/carousels/save")
    public Result save(@RequestBody Carousel carousel){
        if(StringUtils.isEmpty(carousel.getCarouselUrl())
            ||Objects.isNull((carousel.getCarouselRank()))){
            return ResultGenerator.genFailResult("参数异常!");
        }
        String result = carouselService.saveCarousel(carousel);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改
     */
    @PostMapping("/carousels/update")
    @ResponseBody
    public Result update(@RequestBody Carousel carousel){
        if(Objects.isNull(carousel.getCarouselId())||
            StringUtils.isEmpty(carousel.getCarouselUrl())||
            Objects.isNull(carousel.getCarouselRank())){
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = carouselService.updateCarousel(carousel);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }

    }

    /**
     * 详情
     */
    @GetMapping("/carousels/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id){
        Carousel carousel=carouselService.getCarouselById(id);
        if(carousel == null){
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(carousel);
    }

    /**
     * 删除
     */
    @PostMapping("/carousels/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数异常！");
        }
        if(carouselService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }

    }
}
