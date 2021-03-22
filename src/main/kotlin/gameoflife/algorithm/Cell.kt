package gameoflife.algorithm

open class Cell(val x: Int, val y: Int) : Comparable<Cell> {
    private val name = toName(x, y)

    val neighbours: List<Cell> get() = (y - 1..y + 1).flatMap { it.neighboursByRow }

    private val Int.neighboursByRow get() = (x - 1..x + 1).filter { !equals(it, this) }.map { Cell(it, this) }

    private fun equals(column: Int, row: Int): Boolean = x == column && y == row

    override fun equals(other: Any?) = other is Cell && equals(other.x, other.y)
    override fun hashCode() = name.hashCode()
    override fun toString() = name
    override fun compareTo(other: Cell) = name.compareTo(other.name)
}

fun toName(x: Int, y: Int) = "$x|$y"