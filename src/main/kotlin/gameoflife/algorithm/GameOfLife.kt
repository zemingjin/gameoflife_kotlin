package gameoflife.algorithm

import java.util.*

class GameOfLife(private val liveCellsMap: Map<String, Cell>) {
    val liveCells: List<Cell> get() {
        return Optional.of(liveCellsMap)
                .filter { it.isNotEmpty() }
                .map { it.values.toList() }
                .orElseGet { emptyList() }
    }

    private val isReproducible: (Long) -> Boolean = { n -> n == 3L }
    private val isNextGeneration: (Long) -> Boolean = { n -> n in 2..3 }

    fun tick(): GameOfLife = GameOfLife(nextLiveCellsMap)

    private val nextLiveCellsMap: Map<String, Cell> get() {
        return (nextGenerationCells + reproducibleCells)
                .distinct()
                .map { (it.toString() to it) }
                .toList()
                .toMap()
    }

    private val nextGenerationCells get() = filteredCells(liveCells, isNextGeneration)

    private val reproducibleCells get() = filteredCells(inactiveNeighbours, isReproducible)

    val inactiveNeighbours: List<Cell> get() {
        return liveCells
                .map { it.neighbours }
                .flatten()
                .distinct()
                .filter { isInactive(it) }
    }

    private fun filteredCells(cells: List<Cell>, ifNotFiltered: (Long) -> Boolean): List<Cell> {
        return cells.filter { ifNotFiltered(countActiveNeighbours(it)) }
    }

    fun isActive(x: Int, y: Int) = isActive(toString(x, y))

    private fun isActive(key: String) = liveCellsMap[key] != null

    private fun isInactive(cell: Cell) = !isActive(cell.toString())

    private fun countActiveNeighbours(cell: Cell): Long {
        return cell.neighbours.filter { isActive(it.toString()) }.count().toLong()
    }
}