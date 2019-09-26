package gameoflife.helper

import org.junit.Assert.assertEquals
import org.junit.Test

class SeedHelperTest {
    @Test
    fun testSeedToMap() {
        assertEquals("{1|1=1|1, 1|2=1|2, 1|3=1|3}", SeedHelper.seedToMap("1|1, 1|2, 1|3").toString())
    }

    @Test(expected = RuntimeException::class)
    fun testSeedToMapWithNull() {
        SeedHelper.seedToMap((null as String?)!!)
    }

    @Test(expected = RuntimeException::class)
    fun testSetBoundaryWithNull() {
        SeedHelper.seedToMap(arrayOf())
    }

}
