package org.newstudio.util;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ParallelExecutor.
 */
public final class ParallelExecutor {
	private static final AtomicInteger COUNTER = new AtomicInteger(1);
	private final ExecutorService parallel;

	/**
	 * 建立自訂數量的新執行緒池。
	 *
	 * @param count 執行緒數量
	 * @param namingFormat 執行緒池命名規則
	 */
	public ParallelExecutor(int count, @Nonnull String namingFormat) {
		parallel = ExecutorUtil.newFixedThreadPool(
			namingFormat, count
		);
	}

	/**
	 * 建立與 CPU 核心數相符的新執行緒池。
	 *
	 * @param namingFormat 執行緒池命名規則
	 */
	public ParallelExecutor(@Nonnull String namingFormat) {
		this(Runtime.getRuntime().availableProcessors(), namingFormat);
	}

	/**
	 * 建立 CPU 核心數相符的新執行緒池。命名規則為預設。
	 */
	public ParallelExecutor() {
		this("Parallel-" + COUNTER.getAndIncrement() + "-%d");
	}

	/**
	 * 執行工作。
	 *
	 * @param task 工作
	 */
	public void execute(@Nonnull Runnable task) {
		parallel.execute(task);
	}

	/**
	 * 等到所有工作結束。
	 * 執行此方法後 {@link #execute} 就無法再接受工作。
	 */
	public void waitParallel() {
		parallel.shutdown();
		try {
			parallel.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException ignored) {
		}
	}
}
