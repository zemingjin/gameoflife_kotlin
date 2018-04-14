package gameoflife.algorithm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameOfLife {
    private Boundary boundary;
    private final Map<String, Cell> liveCells;

    public GameOfLife(Map<String, Cell> liveCells) {
        this.liveCells = liveCells;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public GameOfLife setBoundary(Boundary boundary) {
        this.boundary = boundary;
        return this;
    }

    public boolean isLiveCell(int x, int y) {
        return liveCells.get(Cell.getString(x, y)) != null;
    }

    private boolean isLiveCell(Cell cell) {
        return isLiveCell(cell.x, cell.y);
    }

    public GameOfLife tick() {
        return new GameOfLife(getNewLiveCellsMap())
                .setBoundary(getBoundary());
    }

    private Map<String, Cell> getNewLiveCellsMap() {
        return Stream.concat(getNextGenerationCells(), getReproductionCells())
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    Collection<Cell> getLiveCells() {
        return Optional.of(liveCells)
                .filter(cells -> !cells.isEmpty())
                .map(Map::values)
                .orElseThrow(() -> new RuntimeException("No more living cells"));
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
        return cell.getNeighbours()
                .filter(this::isLiveCell)
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
        return !isLiveCell(cell);
    }

}