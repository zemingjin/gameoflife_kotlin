package gameoflife.algorithm;

import gameoflife.helper.SeedHelper;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GameOfLifeTest {
    private final SeedHelper seedHelper = new SeedHelper();

    @Test(expected = RuntimeException.class)
    public void testInit() {
        new GameOfLife(new HashMap<>()).getLiveCells();
    }

    private GameOfLife mockGameOfLife(String seed, String boundary) {
        return new GameOfLife(seedHelper.seedToMap(seed)).setBoundary(seedHelper.getBoundary(boundary));
    }

    @Test(expected = RuntimeException.class)
    public void testSeedGameWithEmptyString() {
        mockGameOfLife("", "3|4");
    }

    @Test
    public void testSeed() {
        final GameOfLife gameOfLife = mockGameOfLife("1|1, 1|2, 1|3", "3|4");

        assertEquals(3, gameOfLife.getLiveCells().size());
        assertEquals("[1|1, 1|2, 1|3]", gameOfLife.getLiveCells().toString());
    }

    @Test
    public void testGetDeadCells() {
        final GameOfLife gameOfLife = mockGameOfLife("1|0, 1|1, 1|2", "3|3");

        assertEquals(6, getNeighbouringDeadCellsList(gameOfLife).size());
        assertEquals("[0|0, 2|0, 0|1, 2|1, 0|2, 2|2]",
                     getNeighbouringDeadCellsList(gameOfLife).toString());
    }

    private List<Cell> getNeighbouringDeadCellsList(GameOfLife gameOfLife) {
        return gameOfLife.getInactiveNeighbours().collect(Collectors.toList());
    }

    @Test
    public void testBlinker() {
        GameOfLife gameOfLife = mockGameOfLife("1|0, 1|1, 1|2", "3|3").tick();

        assertEquals("[0|1, 1|1, 2|1]", sort(gameOfLife.getLiveCells()).toString());
        gameOfLife = gameOfLife.tick();
        assertEquals("[1|0, 1|1, 1|2]", sort(gameOfLife.getLiveCells()).toString());
    }

    @Test
    public void testBloker() {
        final GameOfLife gameOfLife = mockGameOfLife("1|1, 1|2, 2|1, 2|2","3|3");

        assertEquals("[1|1, 1|2, 2|1, 2|2]", sort(gameOfLife.tick().getLiveCells()).toString());
    }

    @Test(expected = RuntimeException.class)
    public void testToad() {
        GameOfLife gameOfLife = mockGameOfLife("2|2, 2|3, 3|1, 3|2, 3|3", "4|4");

        gameOfLife = gameOfLife.tick();
        assertEquals("[2|1, 2|3, 3|1, 3|3]", sort(gameOfLife.getLiveCells()).toString());
        gameOfLife = gameOfLife.tick();
        assertEquals("[]", gameOfLife.getLiveCells().toString());
    }

    @Test
    public void testBeacon() {
        GameOfLife gameOfLife = mockGameOfLife("1|1, 1|2, 2|1, 3|4, 4|3, 4|4", "5|5").tick();

        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(gameOfLife.getLiveCells()).toString());
        gameOfLife = gameOfLife.tick();
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", sort(gameOfLife.getLiveCells()).toString());
        gameOfLife = gameOfLife.tick();
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(gameOfLife.getLiveCells()).toString());
    }

    @Test
    public void testGetMaxIndex() {
        assertEquals(new Cell(5, 5),
                     mockGameOfLife("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 8|8", "5|5").getBoundary());
    }

    @Test
    public void testIsLiveCell() {
        final GameOfLife gameOfLife = mockGameOfLife("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 5|5", "5|5");

        assertTrue(gameOfLife.isLiveCell(1, 1));
        assertTrue(gameOfLife.isLiveCell(4, 3));
        assertFalse(gameOfLife.isLiveCell(1, 4));
//        assertFalse(gameOfLife.isLiveCell(5, 5));
    }

    private List<Cell> sort(Collection<Cell> list) {
        return list.stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
