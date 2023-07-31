package com.tian.asenghuamarket.controller.admin;


import com.tian.asenghuamarket.Dto.GoodsCategory;
import com.tian.asenghuamarket.common.CategoryLevelEnum;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.exception.AsengHuaMarketException;
import com.tian.asenghuamarket.service.CategoryService;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.Result;
import com.tian.asenghuamarket.util.ResultGenerator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class GoodsCategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String categoriesPage(HttpServletRequest request,
                                 @RequestParam("categoryLevel")Byte categoryLevel,
                                 @RequestParam("parentId")Long parentId,
                                 @RequestParam("backarentId")Long backParentId){
        if(categoryLevel == null|| categoryLevel<1 ||categoryLevel>3){
            AsengHuaMarketException.fail("参数异常!");
        }
        request.setAttribute("path","category");
        request.setAttribute("parentId",parentId);
        request.setAttribute("backParentId",backParentId);
        request.setAttribute("categoryLevel",categoryLevel);
        return "admin/category";
    }

    /**
     * 列表
     */
    @GetMapping("/categories/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> params){
        if(StringUtils.isEmpty((CharSequence) params.get("page"))||
                StringUtils.isEmpty((CharSequence) params.get("limit"))||
                StringUtils.isEmpty((CharSequence) params.get("categoryLevel"))||
                StringUtils.isEmpty((CharSequence) params.get("parentId"))){
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageQueryUtil=new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(categoryService.getCategoriesPage(pageQueryUtil));
    }

    /**
     * 列表
     */
    @GetMapping("/categories/listForSelect")
    @ResponseBody
    public Result listForSelect(@RequestParam("categoryId")Long categoryId) {
        if (categoryId == null || categoryId < 1) {
            return ResultGenerator.genFailResult("缺少参数！");
        }
        GoodsCategory category = categoryService.getGoodsCategoryById(categoryId);
        //既不是一级分类也不是二级分类
        if (category == null || category.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map categoryResult = new HashMap(4);
        if (category.getCategoryLevel() == CategoryLevelEnum.LEVEL_ONE.getLevel()) {
            //如果是一级分类则返回当前一级分类下的所有二级分类，以及二级分类列表中第一条数据下的所有三级分类
            //查询以及分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), CategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber
                        (Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_THREE.getLevel());
                categoryResult.put("secondLevelCategories", secondLevelCategories);
                categoryResult.put("thirdLevelCategories", thirdLevelCategories);
            }
        }
        if(category.getCategoryLevel()==CategoryLevelEnum.LEVEL_TWO.getLevel()){
            //如果二级分类则返回当前分类下的所有三级分类列表
            List<GoodsCategory>thirdLevelCategories=categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId),CategoryLevelEnum.LEVEL_THREE.getLevel());
            categoryResult.put("thirdLevelCategories",thirdLevelCategories);
        }
        return ResultGenerator.genSuccessResult(categoryResult);
    }

    /**
     * 添加
     */
    @PostMapping("/categories/save")
    @ResponseBody
    public Result save(@RequestBody GoodsCategory goodsCategory){
        if(Objects.isNull(goodsCategory.getCategoryLevel())||
        StringUtils.isEmpty(goodsCategory.getCategoryName())||
        Objects.isNull(goodsCategory.getParentId())||
        Objects.isNull(goodsCategory.getCouponRank())){
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result=categoryService.saveCategory(goodsCategory);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改
     */
    @PostMapping("/categories/update")
    @ResponseBody
    public Result update(@RequestBody GoodsCategory goodsCategory){
        if(Objects.isNull(goodsCategory.getCategoryId())||
        Objects.isNull(goodsCategory.getCategoryLevel())||
        StringUtils.isEmpty(goodsCategory.getCategoryName())||
        Objects.isNull(goodsCategory.getParentId())||
        Objects.isNull(goodsCategory.getCouponRank())){
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = categoryService.updateGoodsCategory(goodsCategory);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/categories/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id")Long id){
        GoodsCategory goodsCategory = categoryService.getGoodsCategoryById(id);
        if(goodsCategory == null){
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(goodsCategory);
    }

    /**
     * 分类删除
     */
    @PostMapping("/categories/delete")
    @ResponseBody
    public Result delte(@RequestBody Integer[]ids){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数异常！");
        }
        if(categoryService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败！");
        }
    }
}
