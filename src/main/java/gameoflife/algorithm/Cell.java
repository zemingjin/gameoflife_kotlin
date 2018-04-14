package gameoflife.algorithm;

import java.awt.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cell extends Point implements Comparable<Cell> {
    private final String string;

    public Cell(int x, int y) {
        super(x, y);
        string = getString(x, y);
    }

    Stream<Cell> getNeighbours() {
        return IntStream.rangeClosed(y - 1, y + 1)
                .mapToObj(this::getNeighboursByRow)
                .flatMap(s -> s);
    }

    private Stream<Cell> getNeighboursByRow(int y) {
        return IntStream.rangeClosed(x - 1, x + 1)
                .mapToObj(x -> new Cell(x, y))
                .filter(this::isNotThis);
    }

    boolean isNeighbour(Cell that) {
        return isNotThis(that) && Math.abs(x - that.x) <= 1 && Math.abs(y - that.y) <= 1;
    }

    private boolean isNotThis(Cell that) {
        return !equals(that);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public int compareTo(Cell that) {
        return toString().compareTo(that.toString());
    }

    static String getString(int x, int y) {
        return String.format("%d|%d", x, y);
    }
}