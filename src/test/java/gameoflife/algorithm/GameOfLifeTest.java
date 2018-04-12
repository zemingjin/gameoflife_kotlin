package gameoflife.algorithm;

import gameoflife.helper.IOHelper;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class GameOfLifeTest {
    private static final int ITERATIONS = 1;
    private static final String PERF_SEEDS = "src/main/resources/sidecar_gun.seed";
    private static final Logger LOG = Logger.getLogger(GameOfLifeTest.class.getName());

    @Test(expected = RuntimeException.class)
    public void testInit() {
        new GameOfLife().getLiveCells();
    }

    @Test(expected = RuntimeException.class)
    public void testSetBoundaryWithNull() {
        new GameOfLife().setBoundary(null);
    }

    @Test(expected = RuntimeException.class)
    public void testSeedGameWithNull() {
        new GameOfLife().setBoundary("3|4").seedGame((String)null);
    }

    @Test(expected = RuntimeException.class)
    public void testSeedGameWithEmptyString() {
        new GameOfLife().setBoundary("3|4").seedGame("");
    }

    @Test
    public void testSeed() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("3|4").seedGame("1|1, 1|2, 1|3");

        assertEquals(3, gameOfLife.getLiveCells().size());
        assertEquals("[1|1, 1|2, 1|3]", gameOfLife.getLiveCells().toString());
    }

    @Test
    public void testGetDeadCells() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("3|3").seedGame("1|0, 1|1, 1|2");

        assertEquals(6, getNeighbouringDeadCellsList(gameOfLife).size());
        assertEquals("[0|0, 2|0, 0|1, 2|1, 0|2, 2|2]",
                     getNeighbouringDeadCellsList(gameOfLife).toString());
    }

    private List<Cell> getNeighbouringDeadCellsList(GameOfLife gameOfLife) {
        return gameOfLife.getNeighbouringDeadCells().collect(Collectors.toList());
    }

    @Test
    public void testBlinker() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("3|3").seedGame("1|0, 1|1, 1|2");

        assertEquals("[0|1, 1|1, 2|1]", sort(gameOfLife.evolve().values()).toString());
        assertEquals("[1|0, 1|1, 1|2]", sort(gameOfLife.evolve().values()).toString());
    }

    @Test
    public void testBloker() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("3|3").seedGame("1|1, 1|2, 2|1, 2|2");

        assertEquals("[1|1, 1|2, 2|1, 2|2]", sort(gameOfLife.evolve().values()).toString());
    }

    @Test
    public void testToad() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("4|4").seedGame("2|2, 2|3, 3|1, 3|2, 3|3");

        assertEquals("[2|1, 2|3, 3|1, 3|3]", sort(gameOfLife.evolve().values()).toString());
        assertEquals("[]", gameOfLife.evolve().values().toString());
    }

    @Test
    public void testBeacon() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("5|5").seedGame("1|1, 1|2, 2|1, 3|4, 4|3, 4|4");

        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(gameOfLife.evolve().values()).toString());
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", sort(gameOfLife.evolve().values()).toString());
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(gameOfLife.evolve().values()).toString());
    }

    @Test
    public void testGetMaxIndex() {
        assertEquals(new Cell(5, 5), new GameOfLife()
                .setBoundary("5|5").seedGame("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 8|8").getBoundary());
    }

    @Test
    public void testIsLiveCell() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("5|5").seedGame("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 5|5");

        assertTrue(gameOfLife.isLiveCell(1, 1));
        assertTrue(gameOfLife.isLiveCell(4, 3));
        assertFalse(gameOfLife.isLiveCell(1, 4));
        assertFalse(gameOfLife.isLiveCell(5, 5));
    }

    @Test
    public void testPerformance() {
        final GameOfLife gameOfLife = new GameOfLife().seedGame(IOHelper.loadSeeds(PERF_SEEDS));
        final long time = System.currentTimeMillis();

        IntStream.range(0, ITERATIONS)
                .forEach(i -> test(gameOfLife));
        LOG.info("Finished in " + IOHelper.format(System.currentTimeMillis() - time));
    }

    private void test(GameOfLife gameOfLife) {
        gameOfLife.evolve();
        final Cell boundary = gameOfLife.getBoundary();
        IntStream.range(0, boundary.getY())
                .forEach(y -> testRow(gameOfLife, y, boundary.getX()));
    }

    private void testRow(GameOfLife gameOfLife, int y, int max) {
        IntStream.range(0, max)
                .forEach(x -> gameOfLife.isLiveCell(x, y));
    }

    private List<Cell> sort(Collection<Cell> list) {
        return list.stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
