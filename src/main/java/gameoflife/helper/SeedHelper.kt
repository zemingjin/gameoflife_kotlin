package gameoflife.helper;

import gameoflife.app.Boundary;
import gameoflife.algorithm.Cell;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SeedHelper {
    private static final String INDICES_DELIMITER = "\\|";
    private static final String CELL_DELIMITER = ", ";
    private static final char LIVE_CELL = 'O';

    /**
     * This method is used only by tests
     * @param seeds the given seeds in the format of "1|1, 1|2, 1|3"
     * @return self
     */
    public Map<String, Cell> seedToMap(String seeds) {
        return Optional.ofNullable(seeds)
                .filter(input -> !input.isEmpty())
                .map(this::seedLiveCells)
                .orElseThrow(() -> new RuntimeException(String.format("Invalid seeds: '%s'", seeds)));
    }

    private Map<String, Cell> seedLiveCells(String liveCells) {
        return Stream.of(liveCells.split(CELL_DELIMITER))
                .map(seed -> getCellFromString(seed, Cell::new))
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    private <T extends Cell> T getCellFromString(String values, BiFunction<Integer, Integer, T> construct) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return construct.apply(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()));
    }

    /**
     * @param seeds the first line contains the size info, such as "#P width|height".
     *              the rest is in the format of ".....OO.O" where the capital 'O' indicate live cell(s).
     * @return this
     */
    public Map<String, Cell> seedToMap(String[] seeds) {
        return seedLiveCells(Arrays.copyOfRange(seeds, 1, seeds.length));
    }

    private Boundary getBoundary(String seed) {
        return getCellFromString(seed, Boundary::new);
    }

    public Boundary getBoundaryFromHeader(String[] seed) {
        return getBoundary(seed[0].split(" ")[1]);
    }

    private Map<String, Cell> seedLiveCells(String[] seeds) {
        return IntStream.range(0, seeds.length)
                .mapToObj(y -> getLiveCellsFromRow(seeds[y], y))
                .flatMap(stream -> stream)
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    private Stream<Cell> getLiveCellsFromRow(String line, int y) {
        return IntStream.range(0, line.length())
                .filter(x -> isLiveCell(line.charAt(x)))
                .mapToObj(x -> new Cell(x, y));
    }

    private boolean isLiveCell(char c) {
        return c == LIVE_CELL;
    }

}
