package gameoflife.algorithm

import gameoflife.helper.SeedHelper
import org.junit.Test
import java.util.HashMap

import org.junit.Assert.*
import kotlin.streams.toList

class GameOfLifeTest {
    private val seedHelper = SeedHelper()

    @Test(expected = RuntimeException::class)
    fun testInit() {
        GameOfLife(HashMap()).getLiveCells()
    }

    private fun mockGameOfLife(seed: String): GameOfLife {
        return GameOfLife(seedHelper.seedToMap(seed))
    }

    @Test(expected = RuntimeException::class)
    fun testSeedGameWithEmptyString() {
        mockGameOfLife("")
    }

    @Test
    fun testSeed() {
        val gameOfLife = mockGameOfLife("1|1, 1|2, 1|3")

        assertEquals(3, gameOfLife.getLiveCells().size.toLong())
        assertEquals("[1|1, 1|2, 1|3]", gameOfLife.getLiveCells().toString())
    }

    @Test
    fun testGetDeadCells() {
        val gameOfLife = mockGameOfLife("1|0, 1|1, 1|2")

        assertEquals(12, getNeighbouringDeadCellsList(gameOfLife).size.toLong())
        assertEquals("[0|-1, 1|-1, 2|-1, 0|0, 2|0, 0|1, 2|1, 0|2, 2|2, 0|3, 1|3, 2|3]",
                getNeighbouringDeadCellsList(gameOfLife).toString())
    }

    private fun getNeighbouringDeadCellsList(gameOfLife: GameOfLife): List<Cell> {
        return gameOfLife.inactiveNeighbours.toList()
    }

    @Test
    fun testBlinker() {
        var gameOfLife = mockGameOfLife("1|0, 1|1, 1|2").tick()

        assertEquals("[0|1, 1|1, 2|1]", sort(gameOfLife.getLiveCells()).toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[1|0, 1|1, 1|2]", sort(gameOfLife.getLiveCells()).toString())
    }

    @Test
    fun testBloker() {
        val gameOfLife = mockGameOfLife("1|1, 1|2, 2|1, 2|2")

        assertEquals("[1|1, 1|2, 2|1, 2|2]", sort(gameOfLife.tick().getLiveCells()).toString())
    }

    @Test(expected = RuntimeException::class)
    fun testToad() {
        var gameOfLife = mockGameOfLife("2|2, 2|3, 3|1, 3|2, 3|3")

        gameOfLife = gameOfLife.tick()
        assertEquals("[2|1, 2|3, 3|1, 3|3, 4|2]", sort(gameOfLife.getLiveCells()).toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[3|1, 3|3, 4|2]", gameOfLife.getLiveCells().toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[3|2, 4|2]", sort(gameOfLife.getLiveCells()).toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("", gameOfLife.getLiveCells().toString())
    }

    @Test
    fun testBeacon() {
        var gameOfLife = mockGameOfLife("1|1, 1|2, 2|1, 3|4, 4|3, 4|4").tick()

        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(gameOfLife.getLiveCells()).toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", sort(gameOfLife.getLiveCells()).toString())
        gameOfLife = gameOfLife.tick()
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(gameOfLife.getLiveCells()).toString())
    }

    @Test
    fun testIsLiveCell() {
        val gameOfLife = mockGameOfLife("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 5|5")

        assertTrue(gameOfLife.isActive(1, 1))
        assertTrue(gameOfLife.isActive(4, 3))
        assertFalse(gameOfLife.isActive(1, 4))
        //        assertFalse(gameOfLife.isActive(5, 5));
    }

    private fun sort(list: Collection<Cell>): List<Cell> {
        return list.stream()
                .sorted()
                .toList()
    }

    @Test
    fun testGetInactiveNeibours() {
        val gameOfLife = mockGameOfLife("1|0, 1|1, 1|2")
        val cells = gameOfLife.inactiveNeighbours.toList()

        assertEquals(12, cells.size.toLong())
        assertEquals("[0|-1, 1|-1, 2|-1, 0|0, 2|0, 0|1, 2|1, 0|2, 2|2, 0|3, 1|3, 2|3]",
                cells.toString())
    }

}