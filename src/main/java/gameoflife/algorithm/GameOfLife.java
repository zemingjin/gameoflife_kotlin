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

    public boolean isActive(int x, int y) {
        return isActive(Cell.toString(x, y));
    }

    private boolean isActive(String key) {
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
                   .filter(c -> isActive(c.toString()))
                   .count();
    }

    Stream<Cell> getInactiveNeighbours() {
        return getLiveCells().stream()
                             .flatMap(Cell::getNeighbours)
                             .filter(this::isNotActive)
                             .distinct();
    }

    private boolean isNotActive(Cell cell) {
        return !isActive(cell.toString());
    }
}