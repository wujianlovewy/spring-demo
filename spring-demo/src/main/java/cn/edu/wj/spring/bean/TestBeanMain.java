package cn.edu.wj.spring.bean;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class TestBeanMain {

	public static void main(String[] args) {
		
		//创建一个BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		
        beanFactory.addBeanPostProcessor(new MyBeanPostProcessor());
        
		//构造一个BeanDefinition
        RootBeanDefinition definition = new RootBeanDefinition(TestBean.class);
        definition.setInitMethodName("myInit");
        definition.setDestroyMethodName("myDestory");
        
        //设置这个bean的属性
        Map<String, Object> map = new HashMap<>();
        map.put("name", "jack");
        definition.setPropertyValues(new MutablePropertyValues(map));

        //注册BeanDefinition
        beanFactory.registerBeanDefinition("testBean", definition);
        
        //创建上下文
        GenericApplicationContext context  = new AnnotationConfigApplicationContext(beanFactory);
        
		context.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());

		context.refresh();
		
        //执行获取和销毁bean的方法
        context.getBean("testBean");
        context.close();
	}
}
