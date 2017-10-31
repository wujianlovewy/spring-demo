package cn.edu.wj.annotation.aspect;

import org.springframework.stereotype.Service;

@Service("cacheService")
public class CacheService implements CacheInterface{

	@LogTest(desc="新增用户")
	public void add(){
		System.out.println("add Cache");
	}
	
}
