package cn.edu.wj.annotation.aspect;

import org.springframework.stereotype.Service;

import cn.edu.wj.annotation.guava.Limiter;

@Service("cacheService")
public class CacheService implements CacheInterface{

	@LogTest(desc="新增用户")
	public void add(){
		System.out.println("add Cache");
	}

	@Limiter(value=100)
	@Override
	public int take() {
		return 0;
	}
	
	
	
}
