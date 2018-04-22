package gameoflife.algorithm;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cell implements Comparable<Cell> {
    private final String string;
    public final int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
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

    private boolean isNotThis(Cell that) {
        return !equals(that);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Cell) {
            Cell that = (Cell)obj;
            return x == that.x && y == that.y;
        }
        return false;
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
        return x + "|" + y;
    }
}