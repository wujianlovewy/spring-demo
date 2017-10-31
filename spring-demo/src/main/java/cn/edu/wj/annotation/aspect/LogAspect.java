package cn.edu.wj.annotation.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

	@Pointcut("execution(* *(..)) && @annotation(cn.edu.wj.annotation.aspect.LogTest)")
	public void doLog(){}
	
	@Pointcut("@annotation(cn.edu.wj.annotation.aspect.LogTest)")
	public void annotation(){}
	
	@Pointcut("@annotation(logTest)")
	public void annotationArgs(LogTest logTest){}
	
	@Pointcut("@within(org.springframework.stereotype.Service)")
	public void withIn(){
	}
	
	//@Around("annotationArgs(logTest)")
	@Around("withIn() && annotationArgs(logTest)")
    public void aroundMethod(ProceedingJoinPoint pjd,LogTest logTest) {
        try {
        	System.out.println("日志: "+logTest.desc());
            System.out.println("前置通知");
            pjd.proceed();
            System.out.println("后置通知");
        } catch (Throwable e) {
            System.out.println("异常通知");
        }
        System.out.println("返回通知");
    }

	
}
