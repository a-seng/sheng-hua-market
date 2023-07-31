package com.tian.asenghuamarket.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SystemUtil {

    private SystemUtil(){}

    public static String getToken(String src){
        if (null == src || "".equals((src))) {
            return null;
        }

        try{
            MessageDigest md=MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            String result=new BigInteger(1,md.digest()).toString(16);
            if(result.length()==31){
                result=result+"-";
            }
            System.out.println(result);
            return result;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }
}
