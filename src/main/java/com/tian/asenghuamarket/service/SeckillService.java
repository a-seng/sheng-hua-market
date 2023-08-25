package com.tian.asenghuamarket.service;

import com.tian.asenghuamarket.Dto.Seckill;
import com.tian.asenghuamarket.controller.vo.ExposerVO;
import com.tian.asenghuamarket.controller.vo.SeckillSuccessVO;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;

import java.util.List;

public interface SeckillService {
    PageResult getSeckillPage(PageQueryUtil pageUtil);

    boolean saveSeckill(Seckill seckill);

    boolean updateSeckill(Seckill seckill);

    Seckill getSeckillById(Long id);

    boolean deleteSeckillById(Long id);

    List<Seckill> getHomeSeckillPage();

    ExposerVO exposerUrl(Long seckillId);

    SeckillSuccessVO executeSeckill(Long seckillId, Long userId);
}
