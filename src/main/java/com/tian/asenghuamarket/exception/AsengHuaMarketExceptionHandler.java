package com.tian.asenghuamarket.exception;


import com.tian.asenghuamarket.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AsengHuaMarketExceptionHandler {

    public static final Logger log= LogFactory.getLogger(AsengHuaMarketExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest req){
        Result result = new Result();
        result.setResultCode(500);
    }

}
