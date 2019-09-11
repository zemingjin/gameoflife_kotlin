package gameoflife.algorithm

open class Cell(val x: Int, val y: Int) : Comparable<Cell> {
    private val string = toString(x, y)

    val neighbours: List<Cell> get() = (y-1..y+1).map { getNeighboursByRow(it) }.flatten()

    private fun getNeighboursByRow(row: Int) = (x-1..x+1).filter { !equals(it, row) }.map { Cell(it, row) }

    private fun equals(column: Int, row: Int) = x == column && y == row

    override fun equals(other: Any?) = if (other is Cell) equals(other.x, other.y) else false

    override fun hashCode() = string.hashCode()

    override fun toString() = string

    override fun compareTo(other: Cell) = string.compareTo(other.string)
}

fun toString(x: Int, y: Int) = "$x|$y"