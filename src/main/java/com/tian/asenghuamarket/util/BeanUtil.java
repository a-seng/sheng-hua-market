package com.tian.asenghuamarket.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanUtil {

    public static Object copyProperties(Object source,Object target,String... ignoreProperties){
        if(source == null){
            return target;
        }
        BeanUtils.copyProperties(source,target,ignoreProperties);
        return target;
    }

    public static <T> List<T> copyList(List sources,Class<T> clazz){
        return copyList(sources,clazz,null);
    }

    private static <T> List<T> copyList(List sources, Class<T> clazz, Object o) {
        List<T> targetList= new ArrayList<>();
        if (sources != null) {
            try{
                for (Object source : sources) {
                    T target = clazz.getDeclaredConstructor().newInstance();
                    copyProperties(source,target);
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
        return targetList;
    }

    public static Map<String,Object> toMap(Object bean,String... ignoreProperties){
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        ArrayList<String> ignoreList = new ArrayList<>(Arrays.asList(ignoreProperties));
        ignoreList.add("class");
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        for (PropertyDescriptor pb : beanWrapper.getPropertyDescriptors()) {
            if(!ignoreList.contains(pb.getName())&& beanWrapper.isReadableProperty(pb.getName())){
                Object propertyValue = beanWrapper.getPropertyValue(pb.getName());
                map.put(pb.getName(),propertyValue);
            }
        }
        return map;

    }

    public static <T> T toBean(Map<String,Object>map,Class<T> beanType){
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(beanType);
        map.forEach((key,value)->{
            if(beanWrapper.isWritableProperty(key)){
                beanWrapper.setPropertyValue(key,value);
            }
        });
        return (T)beanWrapper.getWrappedInstance();
    }

    public interface Callback<T>{
        void set(Object source ,T target);
    }

    //检查pojo对象是否有null字段
    public static boolean checkPojoNullField(Object o,Class<?> clz){
        try{
            Field[] fields=clz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.get(o)== null){
                    return false;
                }
            }
            if(clz.getSuperclass()!=Object.class){
                return checkPojoNullField(o,clz.getSuperclass());
            }
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

    }
}
