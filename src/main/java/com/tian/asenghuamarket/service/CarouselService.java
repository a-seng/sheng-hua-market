package com.tian.asenghuamarket.service;

import com.tian.asenghuamarket.Dto.Carousel;
import com.tian.asenghuamarket.util.PageQueryUtil;

import java.util.List;

public interface CarouselService {

    String getCarouselPage(PageQueryUtil pageQueryUtil);

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<Carousel> getCarouselsForIndex(int number);

    String updateCarousel(Carousel carousel);

    String saveCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    boolean deleteBatch(Integer[] ids);
}
