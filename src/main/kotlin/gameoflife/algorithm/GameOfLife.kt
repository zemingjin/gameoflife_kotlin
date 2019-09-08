package gameoflife.algorithm

import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

class GameOfLife(private val liveCells: Map<String, Cell>) {
    private val isReproducible: (Long) -> Boolean = { n -> n == 3L }
    private val isNextGeneration: (Long) -> Boolean = { n -> n in 2..3 }

    fun getLiveCells(): List<Cell> =
            Optional.of(liveCells)
                .filter { it.isNotEmpty() }
                .map { it.values.toList() }
                .orElseThrow { RuntimeException("No more living cells") }

    fun tick(): GameOfLife = GameOfLife(nextLiveCellsMap)

    private val nextLiveCellsMap
        get() = Stream.concat(nextGenerationCells, reproducibleCells)
                .distinct()
                .map { (it.toString() to it) }
                .toList()
                .toMap()

    private val nextGenerationCells get() = filteredCells(getLiveCells().stream(), isNextGeneration)

    private val reproducibleCells get() = filteredCells(inactiveNeighbours, isReproducible)

    val inactiveNeighbours: Stream<Cell>
        get() = getLiveCells().stream()
                .flatMap { it.neighbours }
                .distinct()
                .filter { isInActive(it) }

    private fun filteredCells(cells: Stream<Cell>, ifotFiltered: (Long) -> Boolean) =
            cells.filter { ifotFiltered(countActiveNeighbours(it)) }

    fun isActive(x: Int, y: Int) = isActive(toString(x, y))

    private fun isActive(key: String) = liveCells[key] != null

    private fun isInActive(cell: Cell) = !isActive(cell.toString())

    private fun countActiveNeighbours(cell: Cell) = cell.neighbours.filter { isActive(it.toString()) }.count()
}