package gameoflife.algorithm

import org.junit.Test

import org.junit.Assert.*
import kotlin.streams.toList

class CellTest {
    @Test
    fun testGetNeighbours() {
        assertEquals(
            "[0|0, 1|0, 2|0, 0|1, 2|1, 0|2, 1|2, 2|2]",
            Cell(1, 1).neighbours.toList().toString()
        )
        assertEquals(
            "[-1|0, 0|0, 1|0, -1|1, 1|1, -1|2, 0|2, 1|2]",
            Cell(0, 1).neighbours.toList().toString()
        )
        assertEquals(
            "[-1|-1, 0|-1, 1|-1, -1|0, 1|0, -1|1, 0|1, 1|1]",
            Cell(0, 0).neighbours.toList().toString()
        )
    }
}
