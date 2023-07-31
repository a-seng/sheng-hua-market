package com.tian.asenghuamarket.service.impl;

import com.tian.asenghuamarket.Dto.Carousel;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.mapper.CarouselMapper;
import com.tian.asenghuamarket.service.CarouselService;
import com.tian.asenghuamarket.util.PageQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;
    @Override
    public String getCarouselPage(PageQueryUtil pageQueryUtil) {
        carouselMapper.
        return null;
    }

    @Override
    public List<Carousel> getCarouselsForIndex(int number) {
        return null;
    }

    @Override
    public String updateCarousel(Carousel carousel) {
        Carousel temp = carouselMapper.selectById(carousel.getCarouselId());
        if(temp == null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        temp.setCarouselRank(carousel.getCarouselRank());
        temp.setRediectUrl(carousel.getCarouselUrl());
        temp.setCarouselUrl(carousel.getCarouselUrl());
        if(carouselMapper.updateById(temp)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String saveCarousel(Carousel carousel) {
        if(carouselMapper.insert(carousel)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Carousel getCarouselById(Integer id) {
        return carouselMapper.selectById(id);
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        if(ids.length<1)
            return false;
        ArrayList<Integer> integers = new ArrayList<>();
        for (Integer id : ids) {
            integers.add(id);
        }
        return carouselMapper.deleteBatchIds((integers))>0;
    }
}
