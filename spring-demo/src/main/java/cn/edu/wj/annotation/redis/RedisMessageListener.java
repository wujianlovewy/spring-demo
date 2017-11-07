package cn.edu.wj.annotation.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisMessageListener implements MessageListener {
	
	@Autowired  
	RedisTemplate<String,String> redisTemplate;  
	
	RedisSerializer<String> serializer;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		serializer  = this.redisTemplate.getStringSerializer();
		String msgBody = this.serializer.deserialize(message.getBody());
		String channel = this.serializer.deserialize(message.getChannel());
		System.out.println("msgBody: "+msgBody+", channel:"+channel);
		if("configParam".equals(channel) && "delete".equals(msgBody)){
			ConfigParamService.map.clear();
		}
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
}
