package com.tian.asenghuamarket.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 首页配置商品VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexCategoryVO implements Serializable {
    private Long categoryId;

    private Byte categoryLevel;

    private String categoryName;

    private List<SecondLevelCategoryVO> secondLevelCategoryVOS;


}
