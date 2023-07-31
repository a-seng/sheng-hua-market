package com.tian.asenghuamarket.exception;

public class AsengHuaMarketException extends RuntimeException{

    public AsengHuaMarketException(){

    }
    public AsengHuaMarketException(String message){
        super(message);
    }

    /**
     * 丢出一个异常
     */
    public static void fail(String message){
        throw new AsengHuaMarketException(message);
    }
}
