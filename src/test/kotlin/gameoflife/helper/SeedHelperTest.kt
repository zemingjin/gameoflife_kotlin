package gameoflife.helper

import org.junit.Test

class SeedHelperTest {
    private val seedHelper = SeedHelper()

    @Test(expected = RuntimeException::class)
    fun testSeedToMapWithNull() {
        seedHelper.seedToMap((null as String?)!!)
    }

    @Test(expected = RuntimeException::class)
    fun testSetBoundaryWithNull() {
        seedHelper.seedToMap(arrayOf())
    }

}
