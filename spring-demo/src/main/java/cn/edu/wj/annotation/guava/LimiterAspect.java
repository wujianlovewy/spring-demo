package cn.edu.wj.annotation.guava;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public final class LimiterAspect {

	@Pointcut("@annotation(limiter)")
	public void annotation(Limiter limiter){}
	
	@Around("annotation(limiter)")
	public void aroundMethod(ProceedingJoinPoint pjd, Limiter limiter){
		System.out.println("限流开始: "+limiter.value());
	}
}
