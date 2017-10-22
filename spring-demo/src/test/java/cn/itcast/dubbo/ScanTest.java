package cn.itcast.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.wj.annotation.ScanClass;
import cn.edu.wj.service.UserService;
import cn.edu.wj.spring.parser.Student;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath:config/spring-annotation.xml"})  
public class ScanTest {
	
	@Qualifier("scanClass")
	@Autowired(required=true)
	private ScanClass scanClass;
	
	@Test
	public void test1(){
		System.out.println("scanClass: "+this.scanClass);
		this.scanClass.test();
	}
	
}
