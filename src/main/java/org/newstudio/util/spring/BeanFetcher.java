package org.newstudio.util.spring;

import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

/**
 * BeanFetcher.
 * 藉由 Spring 初始化時註冊，取得 ApplicationContext 物件，以取得 Spring 管理的 Bean。
 *
 * @author Scribe Huang
 */
public class BeanFetcher implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(final ApplicationContext context) {
		applicationContext = context;
	}

	/**
	 * 依類別型別取得由 Spring 建立管理的 Bean。
	 *
	 * @param classType 類別
	 * @param <T> 型別
	 * @return Bean
	 */
	public static <T> T fetchByClass(final Class<? extends T> classType) {
		T result;
		try {
			result = applicationContext.getBean(classType);
		} catch (NoUniqueBeanDefinitionException e) {
			// Ambiguous: Use default bean naming convention to find.
			result = applicationContext.getBean(
				buildDefaultBeanName(classType), classType
			);
		}
		return result;
	}

	/**
	 * @see AnnotationBeanNameGenerator#buildDefaultBeanName(BeanDefinition)
	 */
	private static String buildDefaultBeanName(final Class<?> classType) {
		String shortClassName = ClassUtils.getShortName(classType.getName());
		return Introspector.decapitalize(shortClassName);
	}
}
