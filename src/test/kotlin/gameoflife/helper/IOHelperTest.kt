package gameoflife.helper

import org.junit.Test

import org.junit.Assert.*

class IOHelperTest {

    @Test
    fun testLoadSeeds() {
        assertEquals(BEACON, loadSeeds(BEACON_FILE).contentToString())
    }

    companion object {
        private const val BEACON_FILE = "src/main/resources/beacon.seed"
        private const val BEACON = "[#P 4|4, OO.., OO.., ..OO, ..OO]"
    }
}
