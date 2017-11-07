package cn.itcast.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.wj.annotation.redis.ConfigParamService;
import cn.edu.wj.annotation.redis.IConfigParam;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath:config/spring-redis.xml"})  
public class RedisTest {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private IConfigParam configParam;
	
	@Test
	public void test1(){
		redisTemplate.convertAndSend("helloredis", "你好subscrible");
		//System.out.println(configParam.getParamNameFromCache("hoho"));
		try {
			while(true){
				System.out.println(configParam.getParamNameFromCache(ConfigParamService.PARAM_KEY));
				Thread.sleep(2000);
			}
			//Thread.sleep(1000000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
