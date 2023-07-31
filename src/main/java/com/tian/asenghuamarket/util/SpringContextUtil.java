package com.tian.asenghuamarket.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringContextUtil implements BeanFactoryPostProcessor {

    /**
     * Spring应用上下本环境
     */
    private static ConfigurableListableBeanFactory beanFactory;

    /**
     * 获取对象
     * @param name
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(String name) throws BeansException{
        return (T) beanFactory.getBean(name);
    }
    /**
     * 获取类型为requiredType的对象
     *
     * @param clz
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        return beanFactory.getBean(clz);
    }
    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     * @param name
     * @return
     */
    public static boolean containsBean(String name){
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype，如果与给定名字相应的bean定义没有被找到将会抛出一个异常（NoSuchBeanDefinitionException）
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.isSingleton(name);
    }

    /**
     * Class注册对象的类型
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException{
        return beanFactory.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     * @param t
     * @param <T>
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static <T> Map<String,T> getBeansOfType(Class<T> t) throws NoSuchBeanDefinitionException{
        return beanFactory.getBeansOfType(t);
    }

    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException{
        return beanFactory.getAliases(name);
    }
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringContextUtil.beanFactory=beanFactory;
    }
}
