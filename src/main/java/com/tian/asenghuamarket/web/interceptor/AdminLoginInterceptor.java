package com.tian.asenghuamarket.web.interceptor;



import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 后台系统身份验证拦截
 * 如果没有登录就会跳转到登录页面
 */
@Component
public class AdminLoginInterceptor implements HandlerInterceptor {


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestServletPath= request.getServletPath();
        if(requestServletPath.startsWith("/admin")&& null== request.getSession().getAttribute("loginUser")){
            request.getSession().setAttribute("errorMsg","亲，你好像还没有登录哦");
            response.sendRedirect(request.getContextPath()+"/admin/login");
            return false;
        }else{
            request.getSession().removeAttribute("errorMsg");
            return true;
        }
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
