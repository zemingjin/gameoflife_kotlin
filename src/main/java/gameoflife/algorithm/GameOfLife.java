package gameoflife.algorithm;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameOfLife {
    private final Map<String, Cell> liveCells;

    public GameOfLife(Map<String, Cell> liveCells) {
        this.liveCells = liveCells;
    }

    public boolean isLiveCell(int x, int y) {
        return isLiveCell(Cell.toString(x, y));
    }

    private boolean isLiveCell(String key) {
        return liveCells.get(key) != null;
    }

    Collection<Cell> getLiveCells() {
        return Optional.of(liveCells)
                .filter(cells -> !cells.isEmpty())
                .map(Map::values)
                .orElseThrow(() -> new RuntimeException("No more living cells"));
    }

    public GameOfLife tick() {
        return new GameOfLife(Stream.concat(getNextGenerationCells(), getReproductionCells())
                                      .collect(Collectors.toMap(Cell::toString, cell -> cell)));
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
                .filter(c -> isLiveCell(c.toString()))
                .count();
    }

    Stream<Cell> getInactiveNeighbours() {
        return getLiveCells().stream()
                .flatMap(this::getInactiveNeighbours)
                .distinct();
    }

    private Stream<Cell> getInactiveNeighbours(Cell cell) {
        return cell.getNeighbours()
                .filter(this::isDeadCell);
    }

    private boolean isDeadCell(Cell cell) {
        return !isLiveCell(cell.toString());
    }
}