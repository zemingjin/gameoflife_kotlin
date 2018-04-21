package gameoflife.algorithm;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
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
        return filteredCells(getLiveCells().stream(), n -> 2 == n || n == 3);
    }

    private Stream<Cell> getReproductionCells() {
        return filteredCells(getInactiveNeighbours(), n -> n == 3);
    }

    private Stream<Cell> filteredCells(Stream<Cell> cells, Predicate<Long> isActiveCell) {
        return cells.filter(cell -> isActiveCell.test(countActiveNeighbours(cell)));
    }

    private long countActiveNeighbours(Cell cell) {
        return cell.getNeighbours()
                .filter(c -> isLiveCell(c.x, c.y))
                .count();
    }

    Stream<Cell> getInactiveNeighbours() {
        return getLiveCells().stream()
                .flatMap(Cell::getNeighbours)
                .filter(this::isDeadCell)
                .distinct();
    }

    private boolean isDeadCell(Cell cell) {
        return !isLiveCell(cell.x, cell.y);
    }
}