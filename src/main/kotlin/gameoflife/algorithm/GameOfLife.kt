package gameoflife.algorithm

class GameOfLife(private val liveCellsMap: Map<String, Cell>) {
    val liveCells: List<Cell> get() =
        liveCellsMap
                .takeIf { it.isNotEmpty() }
                .let { it?.values?.toList() }
                ?: emptyList()

    private val isReproducible: (Int) -> Boolean = { n -> n == 3 }
    private val isNextGeneration: (Int) -> Boolean = { n -> n in 2..3 }

    fun tick(): GameOfLife = GameOfLife(nextLiveCellsMap)

    private val nextLiveCellsMap: Map<String, Cell> get() =
        (nextGenerationCells + reproducibleCells)
                .distinct()
                .map { (it.toString() to it) }
                .toMap()

    private val nextGenerationCells get() = filteredCells(liveCells, isNextGeneration)

    private val reproducibleCells get() = filteredCells(inactiveNeighbours, isReproducible)

    val inactiveNeighbours: List<Cell> get() =
        liveCells.flatMap { it.neighbours }
                .distinct()
                .filter { isInactive(it) }

    private fun filteredCells(cells: List<Cell>, ifNotFiltered: (Int) -> Boolean): List<Cell> =
            cells.filter { ifNotFiltered(countActiveNeighbours(it)) }

    fun isActive(x: Int, y: Int) = isActive(toString(x, y))

    private fun isActive(key: String) = liveCellsMap[key] != null

    private fun isInactive(cell: Cell) = !isActive(cell.toString())

    private fun countActiveNeighbours(cell: Cell): Int =
            cell.neighbours.filter { isActive(it.toString()) }.count()
}