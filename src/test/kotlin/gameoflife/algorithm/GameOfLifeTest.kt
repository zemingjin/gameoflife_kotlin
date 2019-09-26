package gameoflife.algorithm

import gameoflife.helper.SeedHelper
import org.junit.Test
import java.util.HashMap

import org.junit.Assert.*

class GameOfLifeTest {
    @Test
    fun testInit() {
        assertEquals("[]", GameOfLife(HashMap()).liveCells.toString())
    }

    private fun mockGameOfLife(seed: String): GameOfLife {
        return GameOfLife(SeedHelper.seedToMap(seed))
    }

    @Test(expected = RuntimeException::class)
    fun testSeedGameWithEmptyString() {
        mockGameOfLife("")
    }

    @Test
    fun testSeed() {
        val gameOfLife = mockGameOfLife("1|1, 1|2, 1|3")

        assertEquals(3, gameOfLife.liveCells.size.toLong())
        assertEquals("[1|1, 1|2, 1|3]", gameOfLife.liveCells.toString())
    }

    @Test
    fun testGetDeadCells() {
        val gameOfLife = mockGameOfLife("1|0, 1|1, 1|2")

        assertEquals(12, gameOfLife.inactiveNeighbours.size.toLong())
        assertEquals("[0|-1, 1|-1, 2|-1, 0|0, 2|0, 0|1, 2|1, 0|2, 2|2, 0|3, 1|3, 2|3]",
                gameOfLife.inactiveNeighbours.toString())
    }

    @Test
    fun testBlinker() {
        var gameOfLife = mockGameOfLife("1|0, 1|1, 1|2").tick()

        assertEquals("[0|1, 1|1, 2|1]", gameOfLife.liveCells.sorted().toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[1|0, 1|1, 1|2]", gameOfLife.liveCells.sorted().toString())
    }

    @Test
    fun testBloker() {
        val gameOfLife = mockGameOfLife("1|1, 1|2, 2|1, 2|2")

        assertEquals("[1|1, 1|2, 2|1, 2|2]", gameOfLife.tick().liveCells.sorted().toString())
    }


    @Test
    fun testToad() {
        var gameOfLife = mockGameOfLife("2|2, 2|3, 3|1, 3|2, 3|3")

        gameOfLife = gameOfLife.tick()
        assertEquals("[2|1, 2|3, 3|1, 3|3, 4|2]", gameOfLife.liveCells.sorted().toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[3|1, 3|3, 4|2]", gameOfLife.liveCells.toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[3|2, 4|2]", gameOfLife.liveCells.sorted().toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[]", gameOfLife.liveCells.toString())
    }

    @Test
    fun testBeacon() {
        var gameOfLife = mockGameOfLife("1|1, 1|2, 2|1, 3|4, 4|3, 4|4").tick()

        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", gameOfLife.liveCells.sorted().toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", gameOfLife.liveCells.sorted().toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", gameOfLife.liveCells.sorted().toString())
    }

    @Test
    fun testIsLiveCell() {
        val gameOfLife = mockGameOfLife("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 5|5")

        assertTrue(gameOfLife.isActive(1, 1))
        assertTrue(gameOfLife.isActive(4, 3))
        assertFalse(gameOfLife.isActive(1, 4))
    }

    @Test
    fun testGetInactiveNeibours() {
        val gameOfLife = mockGameOfLife("1|0, 1|1, 1|2")

        assertEquals(12, gameOfLife.inactiveNeighbours.size.toLong())
        assertEquals("[0|-1, 1|-1, 2|-1, 0|0, 2|0, 0|1, 2|1, 0|2, 2|2, 0|3, 1|3, 2|3]",
                gameOfLife.inactiveNeighbours.toString())
    }
}
