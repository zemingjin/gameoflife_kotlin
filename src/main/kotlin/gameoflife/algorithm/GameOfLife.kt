package gameoflife.algorithm

import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

class GameOfLife(private val liveCellsMap: Map<String, Cell>) {
    val liveCells: List<Cell> = Optional.of(liveCellsMap)
            .filter { it.isNotEmpty() }
            .map { it.values.toList() }
            .orElseGet { emptyList() }

    private val isReproducible: (Long) -> Boolean = { n -> n == 3L }
    private val isNextGeneration: (Long) -> Boolean = { n -> n in 2..3 }

    fun tick(): GameOfLife = GameOfLife(nextLiveCellsMap)

    private val nextLiveCellsMap
        get() = Stream.concat(nextGenerationCells, reproducibleCells)
                .distinct()
                .map { (it.toString() to it) }
                .toList()
                .toMap()

    private val nextGenerationCells get() = filteredCells(liveCells.stream(), isNextGeneration)

    private val reproducibleCells get() = filteredCells(inactiveNeighbours, isReproducible)

    val inactiveNeighbours: Stream<Cell>
        get() = liveCells.stream()
                .flatMap { it.neighbours }
                .distinct()
                .filter { isInactive(it) }

    private fun filteredCells(cells: Stream<Cell>, ifNotFiltered: (Long) -> Boolean) =
            cells.filter { ifNotFiltered(countActiveNeighbours(it)) }

    fun isActive(x: Int, y: Int) = isActive(toString(x, y))

    private fun isActive(key: String) = liveCellsMap[key] != null

    private fun isInactive(cell: Cell) = !isActive(cell.toString())

    private fun countActiveNeighbours(cell: Cell) = cell.neighbours.filter { isActive(it.toString()) }.count()
}