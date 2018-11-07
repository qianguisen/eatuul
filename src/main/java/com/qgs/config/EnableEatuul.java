package com.qgs.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.qgs.config.EatuulConfiguration;

/**
 * 
 * @Description: 启用eatuul配置
 * @author qianguisen
 * 2018年10月23日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EatuulConfiguration.class)
public @interface EnableEatuul {

}
