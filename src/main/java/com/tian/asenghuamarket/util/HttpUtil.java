package com.tian.asenghuamarket.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

    private static final Logger logger= LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 判断请求是否是ajax请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request){
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

}
