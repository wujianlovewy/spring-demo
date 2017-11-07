package cn.edu.wj.annotation.redis;

public interface IConfigParam {

	public Integer getParamNameFromCache(String key);
	
	public String getMerInfoFromCache(String key);
	
}
