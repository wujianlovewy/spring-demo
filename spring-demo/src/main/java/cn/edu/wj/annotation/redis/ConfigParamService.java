package cn.edu.wj.annotation.redis;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service("configParam")
public class ConfigParamService implements IConfigParam {

	final static Map<String,Integer> map = new ConcurrentHashMap<String, Integer>();
	
	public final static String PARAM_KEY = "nihao";
	
	final static SecureRandom random = new SecureRandom();
	final static int rangeNum = 1000;
	
	@Override
	@Sub(type="", value="configParam")
	public Integer getParamNameFromCache(String key) {
		System.out.println("getParamNameFromCache_key:"+key);
		return buildCacheMap(key);
	}
	
	public synchronized Integer buildCacheMap(String key){
		if(map.get(key)!=null){
			return map.get(key);
		}else{
			int result = random.nextInt(rangeNum);
			map.put(key, result);
			return result;
		}
	}

	@Override
	public String getMerInfoFromCache(String key) {
		return null;
	}

}
