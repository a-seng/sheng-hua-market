package com.tian.asenghuamarket.annotion;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
*reject repeat submit
 */

@Target(ElementType.METHOD) // target of annotation
@Retention(RetentionPolicy.RUNTIME)     //
public @interface RepeatSubmit {
}
