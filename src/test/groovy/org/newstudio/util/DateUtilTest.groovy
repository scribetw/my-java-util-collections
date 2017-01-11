package org.newstudio.util

import mockit.Mock
import mockit.MockUp
import spock.lang.Specification

/**
 * DateUtilTest.
 *
 * @author Scribe Huang
 */
class DateUtilTest extends Specification {
    // Set timezone as test default
    def final static OFFSET = 10
    def final static TZ = TimeZone.getTimeZone("GMT+" + OFFSET)
    def static defaultTZ

    def setupSpec() {
        defaultTZ = TimeZone.default
        TimeZone.setDefault(TZ)
    }

    def cleanupSpec() {
        TimeZone.setDefault(defaultTZ)
    }

    def "can't init directly"() {
        when:
        new DateUtil()

        then:
        thrown(RuntimeException)
    }

    def "test toDateTimeString"() {
        expect:
        DateUtil.toDateTimeString(dateTime) == result

        where:
        dateTime           || result
        dateOf(2013, 1, 1) || "2013-01-01 00:00:00"
    }

    def "test dateOf"() {
        expect:
        DateUtil.dateOf(time) == result

        where:
        time                           || result
        dateOf(2013, 1, 1, 12, 34, 56) || dateOf(2013, 1, 1, 0, 0, 0)
    }

    def "test milliSecondsBetween"() {
        expect:
        DateUtil.milliSecondsBetween(a, b) == result

        where:
        a                  | b                  || result
        dateOf(2013, 1, 1) | dateOf(2013, 1, 1) || 0
        dateOf(2013, 1, 1) | dateOf(2013, 2, 1) || 2678400000
        dateOf(2013, 2, 1) | dateOf(2013, 1, 1) || 2678400000
    }

    def "test secondsBetween"() {
        expect:
        DateUtil.secondsBetween(a, b) == result

        where:
        a                  | b                  || result
        dateOf(2013, 1, 1) | dateOf(2013, 1, 1) || 0
        dateOf(2013, 1, 1) | dateOf(2013, 2, 1) || 2678400
        dateOf(2013, 2, 1) | dateOf(2013, 1, 1) || 2678400
    }

    def "test minutesBetween"() {
        expect:
        DateUtil.minutesBetween(a, b) == result

        where:
        a                  | b                  || result
        dateOf(2013, 1, 1) | dateOf(2013, 1, 1) || 0
        dateOf(2013, 1, 1) | dateOf(2013, 2, 1) || 44640
        dateOf(2013, 2, 1) | dateOf(2013, 1, 1) || 44640
    }

    def "test hoursBetween"() {
        expect:
        DateUtil.hoursBetween(a, b) == result

        where:
        a                  | b                  || result
        dateOf(2013, 1, 1) | dateOf(2013, 1, 1) || 0
        dateOf(2013, 1, 1) | dateOf(2013, 2, 1) || 744
        dateOf(2013, 2, 1) | dateOf(2013, 1, 1) || 744
    }

    def "test daysBetween"() {
        expect:
        DateUtil.daysBetween(a, b) == between

        where:
        a                  | b                  || between
        dateOf(2013, 1, 1) | dateOf(2013, 1, 1) || 0
        dateOf(2013, 1, 1) | dateOf(2013, 2, 1) || 31
        dateOf(2013, 2, 1) | dateOf(2013, 1, 1) || 31
    }

    def "test daysDiff"() {
        expect:
        DateUtil.daysDiff(a, b) == between

        where:
        a                  | b                  || between
        dateOf(2013, 1, 1) | dateOf(2013, 1, 1) || 0
        dateOf(2013, 1, 1) | dateOf(2013, 2, 1) || -31
        dateOf(2013, 2, 1) | dateOf(2013, 1, 1) || 31
    }

    def "test incMilliSecond"() {
        expect:
        DateUtil.incMilliSecond(date, ms) == result

        where:
        date               | ms  || result
        dateOf(2013, 1, 1) | 0   || dateOf(2013, 1, 1)
        dateOf(2013, 1, 1) | 67  || dateOf(2013, 1, 1, 0, 0, 0, 67)
        dateOf(2013, 1, 1) | -67 || dateOf(2012, 12, 31, 23, 59, 59, 933)
    }

    def "test incSecond"() {
        expect:
        DateUtil.incSecond(date, sec) == result

        where:
        date               | sec || result
        dateOf(2013, 1, 1) | 0   || dateOf(2013, 1, 1)
        dateOf(2013, 1, 1) | 67  || dateOf(2013, 1, 1, 0, 1, 7)
        dateOf(2013, 1, 1) | -67 || dateOf(2012, 12, 31, 23, 58, 53)
    }

    def "test incMinute"() {
        expect:
        DateUtil.incMinute(date, min) == result

        where:
        date               | min || result
        dateOf(2013, 1, 1) | 0   || dateOf(2013, 1, 1)
        dateOf(2013, 1, 1) | 67  || dateOf(2013, 1, 1, 1, 7, 0)
        dateOf(2013, 1, 1) | -67 || dateOf(2012, 12, 31, 22, 53, 0)
    }

    def "test incHour"() {
        expect:
        DateUtil.incHour(date, hr) == result

        where:
        date               | hr  || result
        dateOf(2013, 1, 1) | 0   || dateOf(2013, 1, 1)
        dateOf(2013, 1, 1) | 67  || dateOf(2013, 1, 3, 19, 0, 0)
        dateOf(2013, 1, 1) | -67 || dateOf(2012, 12, 29, 5, 0, 0)
    }

    def "test incDay"() {
        expect:
        DateUtil.incDay(date, day) == result

        where:
        date               | day || result
        dateOf(2013, 1, 1) | 0   || dateOf(2013, 1, 1)
        dateOf(2013, 1, 1) | 67  || dateOf(2013, 3, 9)
        dateOf(2013, 1, 1) | -67 || dateOf(2012, 10, 26)
    }

    def "test incWeek"() {
        expect:
        DateUtil.incWeek(date, week) == result

        where:
        date               | week || result
        dateOf(2013, 1, 1) | 0    || dateOf(2013, 1, 1)
        dateOf(2013, 1, 1) | 67   || dateOf(2014, 4, 15)
        dateOf(2013, 1, 1) | -67  || dateOf(2011, 9, 20)
    }

    def "test incMonth"() {
        expect:
        DateUtil.incMonth(date, month) == result

        where:
        date               | month || result
        dateOf(2013, 1, 1) | 0     || dateOf(2013, 1, 1)
        dateOf(2013, 1, 1) | 67    || dateOf(2018, 8, 1)
        dateOf(2013, 1, 1) | -67   || dateOf(2007, 6, 1)
    }

    def "test nowUTC"() {
        given:
        def mock = new MockUp<System>() {
            @Mock
            public static long currentTimeMillis() {
                946728000000 // Sat Jan 01 12:00:00 UTC 2000
            }
        }

        expect:
        DateUtil.nowUTC() == new Date(100, Calendar.JANUARY, 1, 12, 0)


        cleanup:
        mock.tearDown()
    }

    def "test nowLocal"() {
        given:
        def mock = new MockUp<System>() {
            @Mock
            public static long currentTimeMillis() {
                946728000000 // Sat Jan 01 12:00:00 UTC 2000
            }
        }

        expect:
        DateUtil.nowLocal() == new Date(100, Calendar.JANUARY, 1, 12 + OFFSET, 0)

        cleanup:
        mock.tearDown()
    }

    def "test timeZoneOffsetMillis"() {
        expect:
        DateUtil.timeZoneOffsetMillis() == OFFSET * 3600000
    }

    def "test startOfTheMonth"() {
        expect:
        DateUtil.startOfTheMonth(date) == result

        where:
        date                            || result
        dateOf(2013, 1, 1)              || dateOf(2013, 1, 1)
        dateOf(2013, 1, 31, 12, 34, 56) || dateOf(2013, 1, 1)
    }

    def "test endOfTheMonth"() {
        expect:
        DateUtil.endOfTheMonth(date) == result

        where:
        date                            || result
        dateOf(2013, 1, 1)              || dateOf(2013, 1, 31, 23, 59, 59, 999)
        dateOf(2013, 1, 31, 12, 34, 56) || dateOf(2013, 1, 31, 23, 59, 59, 999)
    }

    def "test startOfTheYear"() {
        expect:
        DateUtil.startOfTheYear(date) == result

        where:
        date                            || result
        dateOf(2013, 1, 1)              || dateOf(2013, 1, 1)
        dateOf(2013, 1, 31, 12, 34, 56) || dateOf(2013, 1, 1)
    }

    def "test endOfTheYear"() {
        expect:
        DateUtil.endOfTheYear(date) == result

        where:
        date                            || result
        dateOf(2013, 1, 1)              || dateOf(2013, 12, 31, 23, 59, 59, 999)
        dateOf(2013, 1, 31, 12, 34, 56) || dateOf(2013, 12, 31, 23, 59, 59, 999)
    }

    def "test startOfTheWeek"() {
        expect:
        DateUtil.startOfTheWeek(date) == result

        where:
        date                            || result
        dateOf(2013, 1, 1)              || dateOf(2012, 12, 30)
        dateOf(2013, 1, 31, 12, 34, 56) || dateOf(2013, 1, 27)
    }

    def "test endOfTheWeek"() {
        expect:
        DateUtil.endOfTheWeek(date) == result

        where:
        date                            || result
        dateOf(2013, 1, 1)              || dateOf(2013, 1, 5, 23, 59, 59, 999)
        dateOf(2013, 1, 31, 12, 34, 56) || dateOf(2013, 2, 2, 23, 59, 59, 999)
    }

    def "test startOfTheDay"() {
        expect:
        DateUtil.startOfTheDay(date) == result

        where:
        date                            || result
        dateOf(2013, 1, 1)              || dateOf(2013, 1, 1)
        dateOf(2013, 1, 31, 12, 34, 56) || dateOf(2013, 1, 31)
    }

    def "test endOfTheDay"() {
        expect:
        DateUtil.endOfTheDay(date) == result

        where:
        date                            || result
        dateOf(2013, 1, 1)              || dateOf(2013, 1, 1, 23, 59, 59, 999)
        dateOf(2013, 1, 31, 12, 34, 56) || dateOf(2013, 1, 31, 23, 59, 59, 999)
    }

    def "test compareDate"() {
        expect:
        DateUtil.compareDate(a, b) == result

        where:
        a                              | b                             || result
        dateOf(2013, 1, 1, 12, 34, 56) | dateOf(2013, 1, 1, 21, 43, 0) || DateUtil.EQUAL
        dateOf(2013, 1, 1)             | dateOf(2013, 2, 1)            || DateUtil.BEFORE
        dateOf(2013, 2, 1)             | dateOf(2013, 1, 1)            || DateUtil.AFTER
    }

    def "test compareDateTime"() {
        expect:
        DateUtil.compareDateTime(a, b) == result

        where:
        a                  | b                  || result
        dateOf(2013, 1, 1) | dateOf(2013, 1, 1) || DateUtil.EQUAL
        dateOf(2013, 1, 1) | dateOf(2013, 2, 1) || DateUtil.BEFORE
        dateOf(2013, 2, 1) | dateOf(2013, 1, 1) || DateUtil.AFTER
    }

    def "test delphiTimeToJavaTime"() {
        expect:
        DateUtil.delphiTimeToJavaTime(delphi) == result

        where:
        delphi  || result
        0       || dateOf(1899, 12, 30)
        41275.0 || dateOf(2013, 1, 1)
    }

    def "test javaTimeToDelphiTime"() {
        expect:
        DateUtil.javaTimeToDelphiTime(java) == result

        where:
        java                 || result
        dateOf(2013, 1, 1)   || 41275.0
        dateOf(1899, 12, 30) || 0
        null                 || 0
    }

    def "test fileTimeToJavaTime"() {
        expect:
        DateUtil.fileTimeToJavaTime(time) == result

        where:
        time || result
        0    || dateOf(1601, 1, 1, OFFSET, 0)
    }

    def "test toLocalTime"() {
        expect:
        DateUtil.toLocalTime(utc) == local

        where:
        utc                || local
        dateOf(2013, 1, 1) || dateOf(2013, 1, 1, OFFSET, 0)
    }

    def "test toUtcTime"() {
        expect:
        DateUtil.toUtcTime(local) == utc

        where:
        local                         || utc
        dateOf(2013, 1, 1, OFFSET, 0) || dateOf(2013, 1, 1, 0, 0)
    }

    def dateOf(yyyy, mm, dd, hh = 0, nn = 0, ss = 0, mmm = 0) {
        Calendar c = Calendar.getInstance(TZ)
        c.set(yyyy, mm - 1, dd, hh, nn, ss)
        c.set(Calendar.MILLISECOND, mmm)
        c.time
    }
}
