package gameoflife.algorithm;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameOfLife {
    private static final char LIVE_CELL = 'O';
    private static final String INDICES_DELIMITER = "\\|";
    private static final String CELL_DELIMITER = ", ";

    private Boundary boundary;
    private Map<String, Cell> liveCells;

    /**
     * This method is only called by tests
     * @param boundary the given boundary in the format of "width|height".
     * @return this
     */
    GameOfLife setBoundary(String boundary) {
        return Optional.ofNullable(boundary)
                .map(input -> getCellFromString(input, Boundary::new))
                .map(this::setBoundary)
                .orElseThrow(() -> new RuntimeException("Invalid boundary: null input"));
    }

    /**
     * This method is used only by tests
     * @param seeds the given seeds in the format of "1|1, 1|2, 1|3"
     * @return self
     */
    GameOfLife seedGame(String seeds) {
        return Optional.ofNullable(seeds)
                .filter(input -> !input.isEmpty())
                .map(this::seedLiveCells)
                .map(this::setLiveCells)
                .orElseThrow(() -> new RuntimeException(String.format("Invalid seeds: '%s'", seeds)));
    }

    private Map<String, Cell> seedLiveCells(String seeds) {
        return Stream.of(seeds.split(CELL_DELIMITER))
                .map(seed -> getCellFromString(seed, Cell::new))
                .filter(boundary::isInBound)
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    /**
     *
     * @param seeds the first line contains the size info, such as "#P width|height".
     *              the rest is in the format of ".....OO.O" where the capital 'O' indicate live cell(s).
     * @return this
     */
    public GameOfLife seedGame(String[] seeds) {
        setBoundary(getCellFromString(getBoundaryFromHeader(seeds[0]), Boundary::new));
        return setLiveCells(seedLiveCells(Arrays.copyOfRange(seeds, 1, seeds.length)));
    }

    private String getBoundaryFromHeader(String seed) {
        return seed.split(" ")[1];
    }

    private Map<String, Cell> seedLiveCells(String[] seeds) {
        return IntStream.range(0, Math.min(seeds.length, boundary.getY()))
                .mapToObj(y -> getLiveCellsFromRow(seeds[y], y))
                .flatMap(stream -> stream)
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    private Stream<Cell> getLiveCellsFromRow(String line, int y) {
        return IntStream.range(0, Math.min(line.length(), boundary.getX()))
                .filter(x -> isLiveCell(line.charAt(x)))
                .mapToObj(x -> new Cell(x, y));
    }

    public Boundary getBoundary() {
        return boundary;
    }

    private GameOfLife setBoundary(Boundary boundary) {
        this.boundary = boundary;
        return this;
    }

    private boolean isLiveCell(char c) {
        return c == LIVE_CELL;
    }

    public boolean isLiveCell(int x, int y) {
        return liveCells.get(Cell.getString(x, y)) != null;
    }

    Collection<Cell> getLiveCells() {
        return Optional.of(liveCells)
                .filter(cells -> !cells.isEmpty())
                .map(Map::values)
                .orElseThrow(() -> new RuntimeException("No more living cells"));
    }

    private GameOfLife setLiveCells(Map<String, Cell> liveCells) {
        this.liveCells = liveCells;
        return this;
    }

    public Map<String, Cell> evolve() {
        setLiveCells(tick());
        return liveCells;
    }

    private Map<String, Cell> tick() {
        return Stream.concat(getNextGenerationCells(), getReproductionCells())
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    private Stream<Cell> getNextGenerationCells() {
        return getLiveCells().stream().filter(this::isNextGenerationCell);
    }

    private boolean isNextGenerationCell(Cell cell) {
        final long numberOfNeighbours = getNumberOfLiveNeighbours(cell);
        return 2 == numberOfNeighbours || numberOfNeighbours == 3;
    }

    private Stream<Cell> getReproductionCells() {
        return getNeighbouringDeadCells().filter(cell -> getNumberOfLiveNeighbours(cell) == 3);
    }

    private long getNumberOfLiveNeighbours(Cell cell) {
        return getLiveCells().stream()
                .filter(cell::isNeighbour)
                .count();
    }

    Stream<Cell> getNeighbouringDeadCells() {
        return getLiveCells().stream()
                .flatMap(Cell::getNeighbours)
                .filter(boundary::isInBound)
                .filter(this::isDeadCell)
                .distinct();
    }

    private boolean isDeadCell(Cell cell) {
        return !isLiveCell(cell.getX(), cell.getY());
    }

    private <T extends Cell> T getCellFromString(String values, BiFunction<Integer, Integer, T> construct) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return construct.apply(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()));
    }
}