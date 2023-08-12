package com.tian.asenghuamarket.controller.vo;

import com.tian.asenghuamarket.Dto.GoodsCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 用户秒杀成功VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillSuccessVO implements Serializable {

    private static final long serialVersionUID = 1503814153626594835L;

    private Long seckillSuccessId;

    private String md5;
}
