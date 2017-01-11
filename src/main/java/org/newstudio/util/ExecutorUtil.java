package org.newstudio.util;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 執行緒池工具類別。可協助創建自訂命名規則的執行緒池，以供除錯分辨。
 *
 * @author Scribe Huang
 */
public final class ExecutorUtil {
	private ExecutorUtil() {
		throw new UnsupportedOperationException(
			"Class ExecutorUtil doesn't support new instance."
		);
	}

	/**
	 * 可自訂命名規則的執行緒工廠。
	 *
	 * @see java.util.concurrent.Executors.DefaultThreadFactory
	 */
	public static class NamingThreadFactory implements ThreadFactory {
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String nameFormat;

		/**
		 * 設定新執行緒的命名規則。命名規則中可傳入 %d 作為唯一流水編號。
		 * 例如 "pool-%d" 可產生 "pool-1", "pool-2" 等包含唯一流水編號的名稱。
		 *
		 * @param nameFormat 新執行緒的命名規則
		 */
		public NamingThreadFactory(@Nonnull String nameFormat) {
			SecurityManager s = System.getSecurityManager();
			this.group = (s != null)
				? s.getThreadGroup()
				: Thread.currentThread().getThreadGroup();
			this.nameFormat = nameFormat;
		}

		@Override
		public Thread newThread(@Nonnull Runnable r) {
			Thread t = new Thread(group, r, getThreadName(), 0);
			t.setDaemon(false);
			t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}

		@Nonnull
		private String getThreadName() {
			return String.format(
				nameFormat, threadNumber.getAndIncrement()
			);
		}
	}

	/**
	 * 建立新的執行緒池。
	 *
	 * @param namingFormat 執行緒命名規則，可傳入 %d 作為唯一流水編號
	 * @param nThreads 池中保留執行緒數
	 * @return 新執行緒池
	 * @see java.util.concurrent.Executors#newFixedThreadPool(int)
	 */
	@Nonnull
	public static ExecutorService newFixedThreadPool(@Nonnull String namingFormat,
	                                                 int nThreads) {
		return Executors.newFixedThreadPool(
			nThreads, new NamingThreadFactory(namingFormat)
		);
	}

	/**
	 * 建立新的執行緒池。
	 *
	 * @param namingFormat 執行緒命名規則，可傳入 %d 作為唯一流水編號
	 * @return 新執行緒池
	 * @see java.util.concurrent.Executors#newSingleThreadExecutor()
	 */
	@Nonnull
	public static ExecutorService newSingleThreadExecutor(@Nonnull String namingFormat) {
		return Executors.newSingleThreadExecutor(
			new NamingThreadFactory(namingFormat)
		);
	}

	/**
	 * 建立新的執行緒池。
	 *
	 * @param namingFormat 執行緒命名規則，可傳入 %d 作為唯一流水編號
	 * @return 新執行緒池
	 * @see java.util.concurrent.Executors#newCachedThreadPool()
	 */
	@Nonnull
	public static ExecutorService newCachedThreadPool(@Nonnull String namingFormat) {
		return Executors.newCachedThreadPool(
			new NamingThreadFactory(namingFormat)
		);
	}

	/**
	 * 建立新的排程執行緒池。
	 *
	 * @param namingFormat 執行緒命名規則，可傳入 %d 作為唯一流水編號
	 * @return 新執行緒池
	 * @see java.util.concurrent.Executors#newSingleThreadScheduledExecutor()
	 */
	@Nonnull
	public static ScheduledExecutorService newSingleThreadScheduledExecutor(@Nonnull String namingFormat) {
		return Executors.newSingleThreadScheduledExecutor(
			new NamingThreadFactory(namingFormat)
		);
	}

	/**
	 * 建立新的排程執行緒池。
	 *
	 * @param namingFormat 執行緒命名規則，可傳入 %d 作為唯一流水編號
	 * @param corePoolSize 池中保留執行緒數
	 * @return 新執行緒池
	 * @see java.util.concurrent.Executors#newScheduledThreadPool(int)
	 */
	@Nonnull
	public static ScheduledExecutorService newScheduledThreadPool(@Nonnull String namingFormat,
	                                                              int corePoolSize) {
		return Executors.newScheduledThreadPool(
			corePoolSize, new NamingThreadFactory(namingFormat)
		);
	}
}
