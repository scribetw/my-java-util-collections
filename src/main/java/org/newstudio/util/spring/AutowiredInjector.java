package org.newstudio.util.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.Objects;

/**
 * 後處理 {@link Autowired} 依賴注入標記，以處理非由 Spring 所建立的類別實體但卻想使用依賴注入。
 *
 * 注意：由於是初始化實體事後才處理注入，因此建構元內使用 @Autowired 欄位會產生 NullPointerException。
 */
public final class AutowiredInjector {
	private static AutowireCapableBeanFactory autowireFactory;

	private AutowiredInjector() {
	}

	@Autowired
	public void setAutowireFactory(AutowireCapableBeanFactory factory) {
		autowireFactory = factory;
	}

	/**
	 * 後處理依賴注入 {@link Autowired}。
	 *
	 * @param instance 類別實體
	 * @return 注入後實體
	 */
	public static <T> T autowireInstance(T instance) {
		Objects.requireNonNull(autowireFactory);

		autowireFactory.autowireBean(instance);
		return instance;
	}
}
