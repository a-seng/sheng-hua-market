package com.tian.asenghuamarket.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;


@Data
public class PageQueryUtil extends LinkedHashMap<String,Object> {
    //当前页码
    private int page;
    //每条页数
    private int limit;
    private String sidx;
    private String order;

    public PageQueryUtil(Map<String,Object>params){
        this.putAll(params);

        //分页参数
        this.page=Integer.parseInt(params.get("page").toString());
        this.limit=Integer.parseInt(params.get("limit").toString());
        this.put("start",(page-1)*limit);
        this.put("page",page);
        this.put("limit",limit);
        this.sidx = (String) params.get("sidx");
        this.order = (String)params.get("order");
        if(StringUtils.isNotBlank(sidx)&&StringUtils.isNotEmpty(order)){
            this.put("sortField",this.sidx.replaceAll("[A-Z]","_$0").toLowerCase());
            this.put("order",order);
        }
    }
}
