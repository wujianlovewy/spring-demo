package cn.edu.wj.annotation.guava;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//自定义限流注解
public @interface Limiter {

	int value() default 100;
	
}
