package cn.edu.wj.spring.parser;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		this.registerBeanDefinitionParser("student", new MyBeanDefinitionParser());
	}

}
