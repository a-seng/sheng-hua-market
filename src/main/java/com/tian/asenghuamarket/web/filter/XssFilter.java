package com.tian.asenghuamarket.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XssFilter implements Filter{

    /**
     * 排除链接
     */
    public List<String> excludes= new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String tempExcludes = filterConfig.getInitParameter("exludes");
        if(StringUtils.isNotEmpty(tempExcludes)) {
            String[] url = tempExcludes.split(",");
            excludes.addAll(Arrays.asList(url));
        }
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if(handleExcludeURL(req,resp)){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        XssHttpServletRequestWrapper xssHttpServletRequestWrapper=
                new XssHttpServletRequestWrapper((HttpServletRequest)servletRequest);
        filterChain.doFilter(xssHttpServletRequestWrapper,servletResponse);
    }

    private boolean handleExcludeURL(HttpServletRequest request,HttpServletResponse response){
        String method = request.getMethod();
        //GET DELETE不过滤
        if(method == null || StringUtils.equalsIgnoreCase(method,"GET")||StringUtils.equalsIgnoreCase(method,"DELETE")){
            return true;
        }
        String uri=request.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(uri);
            if(m.find()){
                return true;
            }
        }
        return false;

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
