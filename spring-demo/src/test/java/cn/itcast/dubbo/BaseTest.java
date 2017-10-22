package cn.itcast.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.wj.service.UserService;
import cn.edu.wj.spring.parser.Student;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath:config/spring-parser.xml"})  
public class BaseTest {
	
	
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:config/spring-parser.xml");  
		UserService userService = (UserService)ac.getBean("userService");
		userService.addUser();
	}
	
	@Qualifier("student")
	@Autowired(required=true)
	private Student student;
	
	@Test
	public void test1(){
		System.out.println("student: "+this.student);
		new cn.edu.wj.spring.parser.MyNamespaceHandler();
	}
	
}
