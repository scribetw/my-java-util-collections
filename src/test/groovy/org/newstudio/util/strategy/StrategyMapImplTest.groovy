package org.newstudio.util.strategy

import org.junit.Test

/**
 * StrategyMapImplTest.
 *
 * @author Scribe Huang
 */
class StrategyMapImplTest {
    @Test(expected = IllegalArgumentException)
    void testGetStrategyNull() {
        StrategyMap<Integer, Object> strategies =
            new StrategyMapImpl<>(null, TestNoused)

        strategies.getStrategy(1)
    }

    @Test
    void testGetStrategyNousedNoWith() {
        StrategyMap<Integer, Object> strategies =
            new StrategyMapImpl<>("org.newstudio.util.strategy", TestNoused)

        assert strategies.getStrategy(1) == null
    }

    @Test
    void testGetStrategyValue() {
        StrategyMap<Integer, Object> strategies =
            new StrategyMapImpl<>("org.newstudio.util.strategy", TestValue)

        assert strategies.getStrategy(1) instanceof Test1
        assert strategies.getStrategy(2) instanceof Test2
        assert strategies.getStrategy(3) == null
    }

    @Test
    void testGetStrategyCustom() {
        StrategyMap<String, Object> strategies =
            new StrategyMapImpl<>("org.newstudio.util.strategy", TestCustom)
                .withKeyMethodName("custom")

        assert strategies.getStrategy("A") instanceof TestA
        assert strategies.getStrategy("B") instanceof TestB
        assert strategies.getStrategy("C") == null
    }

    @Test
    void testGetStrategyCustomWithWrongMethodName() {
        StrategyMap<String, Object> strategies =
            new StrategyMapImpl<>("org.newstudio.util.strategy", TestCustom)
                    .withKeyMethodName("custom2")

        assert strategies.getStrategy("A") == null
    }

    @Test
    void testGetStrategyCustomWithInstanceFetcher() {
        StrategyMap<String, Object> strategies =
            new StrategyMapImpl<>("org.newstudio.util.strategy", TestCustom)
                    .withKeyMethodName("custom")
                    .withInstanceFetcher({
                        clazz -> return clazz.newInstance()
                    } as StrategyMapImpl.InstanceFetcher)

        assert strategies.getStrategy("A") instanceof TestA
        assert strategies.getStrategy("B") instanceof TestB
        assert strategies.getStrategy("C") == null
    }

    @Test
    void testGetStrategyInstanceSame() {
        StrategyMap<String, Object> strategies =
            new StrategyMapImpl<>("org.newstudio.util.strategy", TestCustom)
                    .withKeyMethodName("custom")

        def objA1 = strategies.getStrategy("A")
        def objA2 = strategies.getStrategy("A")
        assert objA1.is(objA2)
    }

    @Test(timeout = 5000L)
    void testConcurrentCall() {
        final StrategyMap<String, Object> strategies =
            new StrategyMapImpl<>("org.newstudio.util", TestCustom)
                    .withKeyMethodName("custom")

        Runnable threadWorker = {
            assert strategies.getStrategy("A") != null
        }
        50.times {
            new Thread(threadWorker).start()
        }
    }
}
