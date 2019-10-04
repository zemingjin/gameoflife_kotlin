package gameoflife.algorithm

class GameOfLife(private val liveCellsMap: Map<String, Cell>) {
    val liveCells: List<Cell> get() = if (liveCellsMap.isNotEmpty()) liveCellsMap.values.toList() else emptyList()

    fun tick(): GameOfLife = GameOfLife(nextLiveCellsMap)

    private val nextLiveCellsMap: Map<String, Cell> get() =
        (nextGenerationCells + reproducibleCells)
                .distinct()
                .map { (it.toString() to it) }
                .toMap()

    private val nextGenerationCells get() = liveCells.filteredCells { it in 2..3 }

    private val reproducibleCells get() = inactiveNeighbours.filteredCells { it == 3 }

    val inactiveNeighbours: List<Cell> get() =
        liveCells.flatMap { it.neighbours }
                .distinct()
                .filter { it.isInactive }

    private fun List<Cell>.filteredCells(ifNotFiltered: (Int) -> Boolean): List<Cell> =
            filter { ifNotFiltered(it.countActiveNeighbours) }

    fun isActive(x: Int, y: Int) = toString(x, y).isActive

    private val String.isActive: Boolean get() = liveCellsMap.containsKey(this)

    private val Cell.isInactive: Boolean get() = !toString().isActive

    private val Cell.countActiveNeighbours: Int get() = neighbours.filter { it.toString().isActive }.count()
}