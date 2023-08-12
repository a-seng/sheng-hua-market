package com.tian.asenghuamarket.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存修改所需实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockNumDTO {
    private Long goodsId;

    private Integer goodsCount;
}
