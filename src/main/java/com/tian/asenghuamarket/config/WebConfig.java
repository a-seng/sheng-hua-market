package com.tian.asenghuamarket.config;

import com.tian.asenghuamarket.web.interceptor.AdminLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

}
