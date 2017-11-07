package cn.edu.wj.annotation.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class SubAspect {
	@Pointcut("@annotation(sub)")
	public void annotation(Sub sub){}
	
	@Autowired
	private MessageListener messageListener;
	
	@Autowired
	private RedisMessageListenerContainer redisContainer;
	
	Map<String, String> cachedSubMap = new ConcurrentHashMap<String, String>();
	
	@Before("annotation(sub)")
	public void aroundMethod(Sub sub){
		if(!cachedSubMap.containsKey(sub.value())){
			synchronized(this){
				if(!cachedSubMap.containsKey(sub.value())){
					System.out.println("订阅开始: channel="+sub.value()+", type="+sub.type());
					redisContainer.addMessageListener(messageListener, new ChannelTopic(sub.value()));
					cachedSubMap.put(sub.value(), sub.value());
				}
			}
		}
		
		
		/*Map<MessageListener, Collection<? extends Topic>> listeners = new HashMap<MessageListener, Collection<? extends Topic>>();
		List<Topic> list = new ArrayList<>();
		Topic t = new ChannelTopic(sub.value());
		list.add(t);
		listeners.put(messageListener, list);
		
		this.redisContainer.setMessageListeners(listeners);*/
	}
	
	
}
