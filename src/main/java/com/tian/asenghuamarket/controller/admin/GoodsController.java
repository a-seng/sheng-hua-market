package com.tian.asenghuamarket.controller.admin;


import com.tian.asenghuamarket.Dto.GoodsCategory;
import com.tian.asenghuamarket.Dto.GoodsInfo;
import com.tian.asenghuamarket.common.CategoryLevelEnum;
import com.tian.asenghuamarket.common.Constants;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.exception.AsengHuaMarketException;
import com.tian.asenghuamarket.service.CategoryService;
import com.tian.asenghuamarket.service.GoodsService;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.Result;
import com.tian.asenghuamarket.util.ResultGenerator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class GoodsController {

    @Resource
    private GoodsService goodsService;
    @Resource
    private CategoryService categoryService;

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request){
        request.setAttribute("path","goods");
        return "admin/goods";
    }
    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request){
        request.setAttribute("path","edit");
        //查询所有的一级分类
        List<GoodsCategory> firstLevelCategories = goodsService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel());
        if(!CollectionUtils.isEmpty(firstLevelCategories)){
            //查询一级分类列表中第一个是以的所有二级分类
            List<GoodsCategory> secondLevelCategories =categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_TWO.getLevel());
            if(!CollectionUtils.isEmpty(secondLevelCategories)){
                //查询二级分类列表中的第一个实体类的所有三级分类
                List<GoodsCategory>thirdLevelCategories=categoryService.selectByLevelAndParentIdsAndNumber(
                        Collections.singletonList(secondLevelCategories.get(0).
                                getCategoryId()),
                        CategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories",firstLevelCategories);
                request.setAttribute("secondLevelCategories",secondLevelCategories);
                request.setAttribute("thirdLevelCategories",thirdLevelCategories);
                request.setAttribute("path","goods-edit");
                request.setAttribute("content","");
                return "admin/goods_edit";
            }
        }
        AsengHuaMarketException.fail("分类数据不完善");
        return null;
    }

    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId")Long goodsId){
        request.setAttribute("path","edit");
        GoodsInfo goodsInfo=goodsService.getGoodsById(goodsId);
        if(goodsInfo.getGoodsCategoryId()>0){
            if(goodsInfo.getGoodsCategoryId()!=null||goodsInfo.getGoodsCategoryId()>0){
                //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
                GoodsCategory goodsCategory=categoryService.getGoodsCategoryById(goodsInfo.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if(goodsCategory!=null && goodsCategory.getCategoryLevel()==CategoryLevelEnum.LEVEL_THREE.getLevel()){
                    //查询所有的一级分类
                    categoryService.selectByLevelAndParentIdsAndNumber((Collections.singletonList(0L)),CategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentID查询当前parentId下的所有的三级分裂
                    List<GoodsCategory> thirdCategory=categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(goodsCategory.getParentId()),CategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    GoodsCategory secondCategory = categoryService.getGoodsCategoryById(goodsCategory.getParentId());
                    if(secondCategory!=null){
                        //根据parentId查询当前parentId下的所有二级分类
                        List<GoodsCategory>secondLevelCategories=categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()),CategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        GoodsCategory firstCategory=categoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if(firstCategory!=null){
                            //所有分类都得到之后放倒request对象中供前端读取
                            request.setAttribute("firstLevelCategories",firstCategory);
                            request.setAttribute("secondLevelCategories",secondCategory);
                            request.setAttribute("thirdLevelCategories",thirdCategory);
                            request.setAttribute("firstLevelCategoryId",firstCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId",secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId",goodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if(goodsInfo.getGoodsCategoryId()==0){
            //查询所有的一级分类
            List<GoodsCategory> firstLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel());
            if(!CollectionUtils.isEmpty(firstLevelCategories)){
                //查询一级分类列表中第一个实体的所有二级分类
                List<GoodsCategory>secondLevelCategories=categoryService.selectByLevelAndParentIdsAndNumber(
                        Collections.singletonList(firstLevelCategories.get(0).getCategoryId()),CategoryLevelEnum.LEVEL_TWO.getLevel()
                );
                if(!CollectionUtils.isEmpty(secondLevelCategories)){
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<GoodsCategory>thirdLevelCategories=categoryService.selectByLevelAndParentIdsAndNumber(
                            Collections.singletonList(secondLevelCategories.get(0).getCategoryId(),CategoryLevelEnum.LEVEL_THREE.getLevel()));
                            request.setAttribute("firstLevelCategories",firstLevelCategories);
                            request.setAttribute("secondLevelCategories",secondLevelCategories);
                            request.setAttribute("thirdLevelCategories",thirdLevelCategories);

                }
            }
        }
        request.setAttribute("goods",goodsInfo);
        request.setAttribute("content",goodsInfo.getGoodsDetailContent());
        request.setAttribute("path","goods-edit");
        return "admin/goods_edit";
    }
    /**
     * 列表
     * @return
     */
    @GetMapping("/goods/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object>params){
        if(StringUtils.isEmpty((CharSequence)params.get("page"))||
                StringUtils.isEmpty((CharSequence)params.get("limit"))){
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageQueryUtil=new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(goodsService.getGoodsPage(pageQueryUtil));
    }

    /**
     * 添加
     */
    @PostMapping("/goods/save")
    @ResponseBody
    public Result save(@RequestBody GoodsInfo goodsInfo){
        if(StringUtils.isEmpty(goodsInfo.getGoodsName())
                ||StringUtils.isEmpty(goodsInfo.getGoodsIntro())
                ||StringUtils.isEmpty(goodsInfo.getTag())
                || Objects.isNull(goodsInfo.getOriginalPrice())
                || Objects.isNull(goodsInfo.getGoodsCategoryId())
                || Objects.isNull(goodsInfo.getSellingPrice())
                || Objects.isNull(goodsInfo.getStockNum())
                || Objects.isNull(goodsInfo.getGoodsSellStatus())
                ||StringUtils.isEmpty(goodsInfo.getGoodsCoverImg())
                ||StringUtils.isEmpty(goodsInfo.getGoodsDetailContent())){
            return ResultGenerator.genFailResult("参数异常!");
        }
        String result=goodsService.saveGoods(goodsInfo);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改
     */
    @PostMapping("/goods/update")
    @ResponseBody
    public Result update(@RequestBody GoodsInfo goodsInfo){
        if(Objects.isNull(goodsInfo.getGoodsId())
        ||Objects.isNull(goodsInfo.getOriginalPrice())
        ||Objects.isNull(goodsInfo.getSellingPrice())
        ||Objects.isNull(goodsInfo.getGoodsCategoryId())
        ||Objects.isNull(goodsInfo.getStockNum())
        ||Objects.isNull(goodsInfo.getGoodsSellStatus())
        ||StringUtils.isEmpty(goodsInfo.getGoodsName())
        ||StringUtils.isEmpty(goodsInfo.getGoodsIntro())
        ||StringUtils.isEmpty(goodsInfo.getTag())
        ||StringUtils.isEmpty(goodsInfo.getGoodsCoverImg())
        ||StringUtils.isEmpty(goodsInfo.getGoodsDetailContent())){
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result=goodsService.updateGoods(goodsInfo);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info (@PathVariable("id")Long id){
        GoodsInfo goodsInfo=goodsService.getGoodsById(id);
        return ResultGenerator.genSuccessResult(goodsInfo);
    }

    /**
     * 批量修改销售装填
     */
    @PutMapping("/goods/status/{sellStatus|")
    @ResponseBody
    public Result delete (@RequestBody Long[]ids,@PathVariable("sellStatus")int sellStatus){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数异常!");
        }
        if(sellStatus!= Constants.SELL_STATUS_UP && sellStatus!=Constants.SELL_STATUS_DOWN){
            return ResultGenerator.genFailResult(("状态异常!"));
        }
        if(goodsService.batchUpdateSellStatus(ids,sellStatus)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("修改失败！");
        }
    }
}













