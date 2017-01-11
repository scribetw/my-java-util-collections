package org.newstudio.util

import spock.lang.Specification

/**
 * FileUtilTest.
 *
 * @author Scribe Huang
 */
class FileUtilTest extends Specification {
    def "cannot initiate directly"() {
        when:
        new FileUtil()

        then:
        thrown(RuntimeException)
    }

    def "test sizeByteToKilobyte"() {
        expect:
        FileUtil.sizeByteToKilobyte(size) == expected

        where:
        size              || expected
        0                 || "0.00"
        1024              || "1.00"
        Integer.MAX_VALUE || "2097152.00"
        Long.MAX_VALUE    || "9007199254740992.00"
    }

    def "sizeByteToKilobyte deny negative"() {
        when:
        FileUtil.sizeByteToKilobyte(-1)

        then:
        thrown(RuntimeException)
    }

    def "test copy <File>"() {
        given:
        def src = File.createTempFile("FileUtilTest", ".tmp")
        def dest = File.createTempFile("FileUtilTest", ".tmp")

        and:
        src.write("This is a test.")
        dest.delete()
        assert !dest.exists()

        when:
        def result = FileUtil.copy(src, dest)

        then:
        result == true
        dest.exists()

        cleanup:
        src.delete()
        dest.delete()
    }

    def "test copy <String>"() {
        given:
        def tmpDir = System.getProperty("java.io.tmpdir")
        def src = tmpDir + File.separator + "tmp.tmp"
        def dest = tmpDir + File.separator + "tmp2.tmp"

        and:
        new File(src).createNewFile()
        new File(dest).delete()
        assert !new File(dest).exists()

        when:
        def result = FileUtil.copy(src, dest)

        then:
        result == true
        new File(dest).exists()

        cleanup:
        new File(src).delete()
        new File(dest).delete()
    }

    def "copy deny null"() {
        when:
        FileUtil.copy(src, dest)

        then:
        thrown(RuntimeException)

        where:
        src          | dest
        null         | new File("")
        new File("") | null
        null         | ""
        ""           | null
        null         | null
    }

    def "copy throws Exception"() {
        given:
        def tmpDir = System.getProperty("java.io.tmpdir")
        def src = new File(tmpDir, "tmp.tmp")
        def dest = new File(tmpDir, "tmp2.tmp")

        and:
        src.delete()
        dest.delete()

        when:
        def result = FileUtil.copy(src, dest)

        then:
        result == false
    }
}
