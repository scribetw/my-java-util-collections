package org.newstudio.util

import com.google.common.base.Stopwatch
import spock.lang.Specification

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * ParallelExecutorTest.
 */
class ParallelExecutorTest extends Specification {
    def executor = new ParallelExecutor()

    def "test execute"() {
        given:
        def count = 100
        def counter = new AtomicInteger()

        when:
        count.times {
            executor.execute {
                counter.incrementAndGet()
            }
        }
        executor.waitParallel()

        then:
        counter.intValue() == count
    }

    def "waitParallel blocks execution"() {
        given: "產生無限迴圈，讓執行緒永久等待"
        def LOCKUP_DETECT_TIMEOUT = 1000
        def sw = Stopwatch.createUnstarted()
        def blocker = new Thread({
            executor.execute {
                while (true) {
                    Thread.sleep 100
                }
            }
            executor.waitParallel()
            sw.stop()
        })

        when: "強制中斷執行緒"
        sw.start()
        blocker.start()
        sleepAtLeast(LOCKUP_DETECT_TIMEOUT)
        blocker.interrupt()
        blocker.join(LOCKUP_DETECT_TIMEOUT)

        then: "到強制中斷為止至少要執行 LOCKUP_DETECT_TIMEOUT，而非立刻"
        sw.elapsed(TimeUnit.MILLISECONDS) >= LOCKUP_DETECT_TIMEOUT
    }

    def sleepAtLeast(int ms) {
        final long t0 = System.currentTimeMillis()
        long millisLeft = ms
        while (millisLeft > 0) {
            Thread.sleep(millisLeft)
            long t1 = System.currentTimeMillis()
            millisLeft = ms - (t1 - t0)
        }
    }
}
