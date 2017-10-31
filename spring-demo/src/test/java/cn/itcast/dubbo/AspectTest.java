package cn.itcast.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.wj.annotation.ScanClass;
import cn.edu.wj.annotation.aspect.CacheInterface;
import cn.edu.wj.annotation.aspect.CacheService;
import cn.edu.wj.service.UserService;
import cn.edu.wj.spring.parser.Student;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath:config/spring-aop.xml"})  
public class AspectTest {
	
	@Autowired(required=true)
	private CacheInterface cacheService;
	
	@Test
	public void test1(){
		cacheService.add();
	}
	
}
