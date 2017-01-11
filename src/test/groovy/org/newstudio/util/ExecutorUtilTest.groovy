package org.newstudio.util

import spock.lang.Specification
import spock.lang.Timeout

import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/**
 * ExecutorUtilTest.
 *
 * @author Scribe Huang
 */
class ExecutorUtilTest extends Specification {
    def "Cannot create an instance"() {
        when:
        new ExecutorUtil()

        then:
        thrown(RuntimeException)
    }

    @Timeout(1)
    def "newFixedThreadPool naming startsWith"() {
        given:
        def poolFormat = "TEST-%d"
        def threadName = ""
        def pool = ExecutorUtil.newFixedThreadPool(poolFormat, 2)

        when:
        pool.execute {
            threadName = Thread.currentThread().name
        }
        waitForPoolTerminated(pool)

        then:
        threadName.startsWith(poolFormat.replace("%d", ""))
    }

    @Timeout(1)
    def "newSingleThreadExecutor naming exactly"() {
        given:
        def poolFormat = "TEST"
        def threadName = ""
        def pool = ExecutorUtil.newSingleThreadExecutor(poolFormat)

        when:
        pool.execute {
            threadName = Thread.currentThread().name
        }
        waitForPoolTerminated(pool)

        then:
        threadName == poolFormat
    }

    @Timeout(1)
    def "newCachedThreadPool naming startsWith"() {
        given:
        def poolFormat = "TEST-%d"
        def threadName = ""
        def pool = ExecutorUtil.newCachedThreadPool(poolFormat)

        when:
        pool.execute {
            threadName = Thread.currentThread().name
        }
        waitForPoolTerminated(pool)

        then:
        threadName.startsWith(poolFormat.replace("%d", ""))
    }

    @Timeout(1)
    def "newSingleThreadScheduledExecutor naming exactly"() {
        given:
        def poolFormat = "TEST"
        def threadName = ""
        def pool = ExecutorUtil.newSingleThreadScheduledExecutor(poolFormat)

        when:
        pool.execute {
            threadName = Thread.currentThread().name
            assert threadName.startsWith(poolFormat)
        }
        waitForPoolTerminated(pool)

        then:
        threadName == poolFormat
    }

    @Timeout(1)
    def "newScheduledThreadPool naming startsWith"() {
        given:
        def poolFormat = "TEST-%d"
        def threadName = ""
        def pool = ExecutorUtil.newScheduledThreadPool(poolFormat, 2)

        when:
        pool.execute {
            threadName = Thread.currentThread().name
        }
        waitForPoolTerminated(pool)

        then:
        threadName.startsWith(poolFormat.replace("%d", ""))
    }

    def waitForPoolTerminated(ExecutorService pool) {
        pool.shutdown()
        while (!pool.awaitTermination(100, TimeUnit.NANOSECONDS)) {
            // no-op
        }
    }
}
