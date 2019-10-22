package gameoflife.helper

import org.junit.Assert.*
import org.junit.Test

class ArgsHelperTest {
    @Test
    fun testWaitTime() {
        assertEquals(200, arrayOf("-w200").waitTime)
        assertEquals(100, emptyArray<String>().waitTime)
    }

    @Test
    fun testIsAutomation() {
        assertFalse(arrayOf("-s").isAutomaton)
        assertTrue(arrayOf("-ss").isAutomaton)
    }
}