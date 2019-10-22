package gameoflife.helper

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeHelperTest {
    @Test
    fun testToTime() {
        println("TimeHelperTest.testToTime")
        assertEquals("0:00:01.000", 1000.toLong().toTime)
        assertEquals("0:00:00.123", 123.toLong().toTime)
        assertEquals("0:01:01.000", 61000.toLong().toTime)
        assertEquals("1:01:01.000", 3661000.toLong().toTime)
        assertEquals("10:01:01.000", 36061000.toLong().toTime)
    }
}