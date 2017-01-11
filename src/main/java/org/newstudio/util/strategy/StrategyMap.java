package org.newstudio.util.strategy;

import javax.annotation.Nullable;

/**
 * 策略模式對應表介面。
 *
 * @author Scribe Huang
 * @param <K> 策略鍵值型別
 * @param <V> 策略物件型別
 */
public interface StrategyMap<K, V> {
	/**
	 * 取得對應的策略物件。
	 *
	 * @param key 策略鍵值
	 * @return 策略物件
	 */
	@Nullable
	V getStrategy(K key);
}
