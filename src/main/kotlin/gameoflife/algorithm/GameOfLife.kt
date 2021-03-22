package gameoflife.algorithm

class GameOfLife(private val livingCellsMap: Map<String, Cell>) {
    val livingCells get() = if (livingCellsMap.isNotEmpty()) livingCellsMap.values.toList() else emptyList()

    fun tick(): GameOfLife = GameOfLife(nextMap)

    private val nextMap: Map<String, Cell>
        get() =
            (nextGenerationCells + reproducibleCells)
                .distinct()
                .map { (it.toString() to it) }
                .toMap()

    private val nextGenerationCells get() = livingCells.filteredCells { it in 2..3 }
    private val reproducibleCells get() = activeNeighbours.filteredCells { it == 3 }
    private fun List<Cell>.filteredCells(filtered: (Int) -> Boolean) = filter { filtered(it.countActiveNeighbours) }

    private val String.isActive: Boolean get() = livingCellsMap[this] is Cell
    private val Cell.isInactive: Boolean get() = !toString().isActive
    private val Cell.countActiveNeighbours: Int get() = neighbours.filter { it.toString().isActive }.count()

    val activeNeighbours get() = livingCells.flatMap { it.neighbours }.distinct().filter { it.isInactive }

    fun isActive(x: Int, y: Int) = toName(x, y).isActive
}