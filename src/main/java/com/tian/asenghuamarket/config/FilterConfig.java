package com.tian.asenghuamarket.config;


import com.tian.asenghuamarket.web.filter.XssFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter配置
 */
@Configuration
@ConditionalOnProperty(value = "xss.enabled",havingValue = "true")
public class FilterConfig {

    @Value("${xss.exclueds")
    private String excludes;

    @Value("${xss.urlPatterns}")
    private String urlPatterns;

    @Bean
    public FilterRegistrationBean<Filter>xssFilterRegistration(){
        FilterRegistrationBean<Filter> registrationBean=new FilterRegistrationBean<>();
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        registrationBean.setFilter(new XssFilter());
        registrationBean.addUrlPatterns(StringUtils.split(urlPatterns,","));
        registrationBean.setName("xxsFilter");
        registrationBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("excludes",excludes);
        registrationBean.setInitParameters(initParameters);
        return registrationBean;

    }

}
