package gameoflife.algorithm

import java.util.stream.Stream

open class Cell(val x: Int, val y: Int) : Comparable<Cell> {
    private val string = toString(x, y)

    val neighbours: Stream<Cell> get() = (y-1..y+1).stream.flatMap { getNeighboursByRow(it) }

    private fun getNeighboursByRow(row: Int) = (x-1..x+1).stream.filter { isNotThis(it, row) }.map { Cell(it, row) }

    private fun isNotThis(column: Int, row: Int) = x != column || y != row

    override fun hashCode() = string.hashCode()

    override fun equals(other: Any?) =
            when {
                (other === this) -> true
                (other is Cell) -> x == other.x && y == other.y
                else -> false
            }

    override fun toString() = string

    override fun compareTo(other: Cell) = string.compareTo(other.string)
}

val <T> Iterable<T>.stream get() = toList().stream()

fun toString(x: Int, y: Int) = "$x|$y"