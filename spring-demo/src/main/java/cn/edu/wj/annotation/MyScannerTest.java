package cn.edu.wj.annotation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * 通过spring扫描包处理注解
 * @author wuj
 * 参考：http://blog.csdn.net/chjttony/article/details/6301523 spring对注解源码分析
 *   http://blog.csdn.net/bolg_hero/article/details/76038750 自定义注解解析
 */
@Configuration
public class MyScannerTest {
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(MyScannerTest.class);
		annotationConfigApplicationContext.refresh();
		ScanClass injectClass = annotationConfigApplicationContext.getBean(ScanClass.class);
		injectClass.test();
	}
	
	@Component
	public static class BeanScannerConfigurer implements BeanFactoryPostProcessor, ApplicationContextAware {

		private ApplicationContext applicationContext;

		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
		}

		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
			Reflections reflections = new Reflections("cn.edu.wj");
			Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SPI.class);
			if(null!=annotated){
				BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
				for (Class<?> serviceClass : annotated) {  
					  if(serviceClass.isAnnotationPresent(SPI.class)){
						  RootBeanDefinition beanDefinition = new RootBeanDefinition();  
						  //beanDefinition.setBeanClass(serviceClass);
						  //beanDefinitionRegistry.registerBeanDefinition(ClassUtils.getShortName(serviceClass), beanDefinition);
						  
						  //通过factoryBean生成
						  beanDefinition.getPropertyValues().add("innerClassName", ClassUtils.getQualifiedName(serviceClass));
						  beanDefinition.setBeanClass(FactoryBeanTest.class);
						  beanDefinitionRegistry.registerBeanDefinition(ClassUtils.getShortName(FactoryBeanTest.class), beanDefinition);
					  }
				}
			}
			
			//通过构造spring beandefinition
			
			//通过AnnotatedBeanDefinitionReader注册bean
			/*if(null!=annotated){
				AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader((BeanDefinitionRegistry) beanFactory);
				for (Class<?> serviceClass : annotated) {  
					  if(serviceClass.isAnnotationPresent(SPI.class)){
						  reader.register(serviceClass);
					  }
				}
			}*/
			
			//通过扫描类实现注解自定义
			/*Scanner scanner = new Scanner((BeanDefinitionRegistry) beanFactory);
			scanner.setResourceLoader(this.applicationContext);
			scanner.scan("cn.edu.wj.annotation");*/
		}
	}

	public final static class Scanner extends ClassPathBeanDefinitionScanner {

		public Scanner(BeanDefinitionRegistry registry) {
			super(registry);
		}

		public void registerDefaultFilters() {
			this.addIncludeFilter(new AnnotationTypeFilter(SPI.class));
		}

		public Set<BeanDefinitionHolder> doScan(String... basePackages) {
			Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
			for (BeanDefinitionHolder holder : beanDefinitions) {
				GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
				definition.getPropertyValues().add("innerClassName", definition.getBeanClassName());
				definition.setBeanClass(FactoryBeanTest.class);
			}
			return beanDefinitions;
		}

		public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return super.isCandidateComponent(beanDefinition) && beanDefinition.getMetadata()
					.hasAnnotation(SPI.class.getName());
		}

	}

	public static class FactoryBeanTest<T> implements InitializingBean, FactoryBean<T> {

		private String innerClassName;

		public void setInnerClassName(String innerClassName) {
			this.innerClassName = innerClassName;
		}

		public T getObject() throws Exception {
			Class innerClass = Class.forName(innerClassName);
			if (innerClass.isInterface()) {
				return (T) InterfaceProxy.newInstance(innerClass);
			} else {
				Enhancer enhancer = new Enhancer();
				enhancer.setSuperclass(innerClass);
				enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
				enhancer.setCallback(new MethodInterceptorImpl());
				return (T) enhancer.create();
			}
		}

		public Class<?> getObjectType() {
			try {
				return Class.forName(innerClassName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}

		public boolean isSingleton() {
			return true;
		}

		public void afterPropertiesSet() throws Exception {

		}
	}

	public static class InterfaceProxy implements InvocationHandler {

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			System.out.println("ObjectProxy execute:" + method.getName());
			return method.invoke(proxy, args);
		}

		public static <T> T newInstance(Class<T> innerInterface) {
			ClassLoader classLoader = innerInterface.getClassLoader();
			Class[] interfaces = new Class[] { innerInterface };
			InterfaceProxy proxy = new InterfaceProxy();
			return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
		}
	}

	public static class MethodInterceptorImpl implements MethodInterceptor {

		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			System.out.println("MethodInterceptorImpl:" + method.getName());
			return methodProxy.invokeSuper(o, objects);
		}

	}
}
