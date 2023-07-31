package com.tian.asenghuamarket.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PatternUtil {
    /**
     * 匹配邮箱正则
     */
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"),  Pattern.CASE_INSENSITIVE);


    public static Boolean validKeyword(String keyword){
        String regex="^[a-zA-Z0-9]+$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher = pattern.matcher(keyword);
        return matcher.matches();
    }


    /**
     * 判断是否是邮箱
     * @param emailStr
     * @return
     */
    public static boolean isEmail(String emailStr){
        Matcher matcher=VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean isURL(String urlString){
        String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+(\\??(([A-Za-z0-9-~]+=?)([A-Za-z0-9-~]*)&?)*)$";
//        String re="^[hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+(A-Za-z0-9-~/])+(\\??(([A-Za-z0-9-~]+=?)([A-Za-z0-9-~]*)&?)*)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(urlString).matches();
    }

}
