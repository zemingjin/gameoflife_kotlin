package gameoflife.helper

import org.junit.Assert.assertEquals
import org.junit.Test

class ArgsHelperTest {
    @Test
    fun testWaitTime() {
        assertEquals(200, arrayOf("-w200").waitTime)
        assertEquals(100, emptyArray<String>().waitTime)
    }
}