package cn.edu.wj.annotation.redis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//自定义订阅redis注解
public @interface Sub {

	String value();//topicName
	
	String type(); //ChannelTopic或者PatternTopic
	
}
