package com.tian.asenghuamarket.controller.vo;

import com.tian.asenghuamarket.common.SeckillStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 购物车页面购物项VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExposerVO implements Serializable {
    private static final long serialVersionUID = -7615136662052646516L;
    // 秒杀状态enum
    private SeckillStatusEnum seckillStatusEnum;

    // 一种加密措施
    private String md5;

    // id
    private long seckillId;

    // 系统当前时间（毫秒）
    private long now;

    // 开启时间
    private long start;

    // 结束时间
    private long end;

    public ExposerVO(SeckillStatusEnum seckillStatusEnum, String md5, long seckillId) {
        this.seckillStatusEnum = seckillStatusEnum;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public ExposerVO(SeckillStatusEnum seckillStatusEnum, long seckillId, long now, long start, long end) {
        this.seckillStatusEnum = seckillStatusEnum;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public ExposerVO(SeckillStatusEnum seckillStatusEnum, long seckillId) {
        this.seckillStatusEnum = seckillStatusEnum;
        this.seckillId = seckillId;
    }

}
