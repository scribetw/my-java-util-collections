package org.newstudio.util

import spock.lang.Specification

/**
 * PairTest.
 */
class PairTest extends Specification {
    def "Static of method creates object"() {
        when:
        def p = Pair.of(1, "Hello")

        then:
        p._1 == 1
        p._2 == "Hello"
    }

    def "test toString"() {
        expect:
        Pair.of(0, 0).toString() == "(0, 0)"
        Pair.of("Hello", "World").toString() == "(Hello, World)"
    }

    def "test swap"() {
        given:
        def p = Pair.of(1024, 768)

        when:
        def swapped = p.swap()

        then:
        swapped._1 == 768
        swapped._2 == 1024
    }

    def "test equals"() {
        given:
        def p1 = Pair.of(0, 0)
        def p2 = Pair.of(0, 0)

        expect:
        p1 == p2
    }

    def "test hashCode"() {
        given:
        def p1 = Pair.of(1, 0)
        def p2 = Pair.of(0, 1)

        expect:
        p1.hashCode() != p2.hashCode()
    }
}
