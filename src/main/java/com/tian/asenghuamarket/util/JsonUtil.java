package com.tian.asenghuamarket.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 操作json的封装方法
 */
public class JsonUtil {

    /**
     * 将json字符串转化为java对象
     * @param objClass
     * @param jsonStr
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T jsonToObj(Class<T> objClass,String jsonStr) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr,objClass);
    }

    /**
     * 将obj对象转为json字符串
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String objToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
