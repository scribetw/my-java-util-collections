package org.newstudio.util.strategy;

import org.newstudio.util.spring.BeanFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略類別對應表實作。Thread-safe。
 *
 * @author Scribe Huang
 * @param <K> 策略鍵值型別
 * @param <V> 策略物件型別
 */
public final class StrategyMapImpl<K, V> implements StrategyMap<K, V> {
	private static Logger LOG = LoggerFactory.getLogger(StrategyMapImpl.class);

	private Map<K, Class<V>> strategies = Collections.emptyMap();
	private final Map<K, V> cacheInstances = new ConcurrentHashMap<K, V>();
	private volatile boolean notScanned = true;

	private final String packageScope;
	private final Class<? extends Annotation> annotationToScan;
	private String annotationKeyMethodName = "value";
	private InstanceFetcher<V> instanceFetcher = new InstanceFetcher<V>() {
		@Override
		public V getInstance(final Class<V> className) throws Exception {
			return className.newInstance();
		}
	};
	private final InstanceFetcher<V> FROM_SPRING = new InstanceFetcher<V>() {
		@Override
		public V getInstance(final Class<V> className) throws Exception {
			return BeanFetcher.fetchByClass(className);
		}
	};

	/**
	 * 指定要掃瞄的標註類別及包裹範圍，並建立策略對應表。
	 *
	 * @param scope 掃瞄包裹範圍
	 * @param scanAnnotation 標註類別物件
	 */
	public StrategyMapImpl(final String scope,
	                       final Class<? extends Annotation> scanAnnotation) {
		packageScope = scope;
		annotationToScan = scanAnnotation;
	}

	/**
	 * 指定標註取得鍵值方法名稱。
	 * 如果策略的鍵值如 {@code @Strategy(key = 1)}，則方法名稱為 "key"。
	 *
	 * @param keyMethodName 標註取得鍵值方法名稱
	 * @return 設定鍊 (自身物件)
	 */
	public StrategyMapImpl<K, V> withKeyMethodName(final String keyMethodName) {
		annotationKeyMethodName = keyMethodName;
		return this;
	}

	/**
	 * 指定類別取得實體物件。
	 *
	 * @param fetcher 類別取得實體物件
	 * @return 設定鍊 (自身物件)
	 */
	public StrategyMapImpl<K, V> withInstanceFetcher(final InstanceFetcher<V> fetcher) {
		instanceFetcher = fetcher;
		return this;
	}

	/**
	 * 指定自 Spring Bean 取得實體物件。
	 *
	 * @return 設定鍊 (自身物件)
	 */
	public StrategyMapImpl<K, V> withInstanceFetcherFromSpring() {
		withInstanceFetcher(FROM_SPRING);
		return this;
	}

	@SuppressWarnings("unchecked")
	private void detect() {
		if (packageScope == null) {
			throw new IllegalArgumentException("packageScope must be set.");
		}

		Map<K, Class<V>> strategiesInternal = new HashMap<K, Class<V>>();

		Collection<Class<?>> classes = new HashSet<Class<?>>();
		ClassLoader cl = ClassPathScanningCandidateComponentProvider.class.getClassLoader();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(annotationToScan));
		for (BeanDefinition bd : scanner.findCandidateComponents(packageScope)) {
			try {
				classes.add(cl.loadClass(bd.getBeanClassName()));
			} catch (ClassNotFoundException e) {
				LOG.debug("", e);
			}
		}
		for (Class<?> c : classes) {
			try {
				Annotation a = c.getAnnotation(annotationToScan);
				Method m = annotationToScan.getDeclaredMethod(annotationKeyMethodName);
				strategiesInternal.put((K) m.invoke(a), (Class<V>) c);
				LOG.debug("Scanning {} with {}", c, a);
			} catch (Exception e) {
				LOG.error("", e);
			}
		}

		LOG.debug("Detect finished. result = {}", strategiesInternal.size());
		if (strategiesInternal.isEmpty()) {
			LOG.debug(
				"Make sure the annotation is @Retention(RUNTIME) or the package is correct."
			);
		}
		strategies = Collections.unmodifiableMap(strategiesInternal);
	}

	@Override
	@Nullable
	public V getStrategy(final K key) {
		scan();

		V strategyInstance = null;
		try {
			Class<V> strategyClass = strategies.get(key);
			LOG.debug("Found Class {} with key {}.", strategyClass, key);
			if (strategyClass != null) {
				strategyInstance = cacheInstances.get(key);
				if (strategyInstance == null) {
					strategyInstance = instanceFetcher.getInstance(strategyClass);
					cacheInstances.put(key, strategyInstance);
				}
			} else {
				LOG.debug("Can not get strategy: {}", key);
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return strategyInstance;
	}

	/**
	 * 進行標註掃瞄。
	 * 掃瞄使用 Lazy initialization，在初次呼叫 {@link #getStrategy(K)} 時才會掃瞄。
	 */
	@GuardedBy("synchronized")
	private void scan() {
		if (notScanned) {
			synchronized (this) {
				if (notScanned) {
					detect();
					notScanned = false;
				}
			}
		} else {
			LOG.debug("Use cached scan result.");
		}
	}

	/**
	 * 類別取得實體介面。
	 *
	 * @param <V> 策略物件型別
	 */
	public interface InstanceFetcher<V> {
		/**
		 * 根據所傳入的類別物件，回傳類別實體。
		 *
		 * @param className 類別
		 * @return 實體物件
		 * @throws Exception 如果無法產生
		 */
		V getInstance(Class<V> className) throws Exception;
	}
}
