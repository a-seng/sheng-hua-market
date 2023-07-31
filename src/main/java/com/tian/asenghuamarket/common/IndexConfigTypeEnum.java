package com.tian.asenghuamarket.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum IndexConfigTypeEnum {

    DEFAULT(0,"DEFAULT"),
    INDEX_SEARCH_HOTS(1,"INDEX_SEARCH_HOTS"),
    INDEX_SEARCH_DOWN_HOTS(2,"INDEX-SEARCH_DOWN_HOTS"),
    INDEX_GOODS_HOT(3,"INDEX_GOODS_HOTS"),
    INDEX_GOODS_NEW(4,"INDEX_GOODS_NEW"),
    INDEX_GOODS_RECOMMOND(5,"INDEX_GOODS_RECOMMOND");

    public void setType(int type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    private int getType() {
        return type;
    }


    private int type;

    private String name;

    public static IndexConfigTypeEnum getIndexConfigTypeEnumByType(int type){
        for (IndexConfigTypeEnum value : IndexConfigTypeEnum.values()) {
            if(value.getType()== type){
                return value;
            }
        }
        return DEFAULT;
    }



}
