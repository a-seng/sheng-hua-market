package com.tian.asenghuamarket.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 首页配置商品VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexCarouselVO implements Serializable {

    private String carouselUrl;

    private String redirectUrl;

}
