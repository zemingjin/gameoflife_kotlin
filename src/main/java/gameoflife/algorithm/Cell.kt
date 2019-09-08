package gameoflife.algorithm

import java.util.stream.IntStream
import java.util.stream.Stream

open class Cell(val x: Int, val y: Int) : Comparable<Cell> {
    private val string = toString(x, y)

    val neighbours: Stream<Cell>
        get() = IntStream.rangeClosed(y - 1, y + 1)
                .mapToObj { getNeighboursByRow(it) }
                .flatMap { it }

    private fun getNeighboursByRow(row: Int) =
            IntStream.rangeClosed(x - 1, x + 1)
                .filter {isNotThis(it, row) }
                .mapToObj { Cell(it, row) }

    private fun isNotThis(column: Int, row: Int) = x != column || y != row

    override fun hashCode() = string.hashCode()

    override fun equals(other: Any?) =
            when {
                (other === this) -> true
                (other is Cell) -> x == other.x && y == other.y
                else -> false
            }

    override fun toString() = string

    override fun compareTo(other: Cell) = toString().compareTo(other.toString())
}

fun toString(x: Int, y: Int): String = "$x|$y"