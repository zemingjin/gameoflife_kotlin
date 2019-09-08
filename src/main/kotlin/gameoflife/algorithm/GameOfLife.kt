package gameoflife.algorithm

import java.util.Optional
import java.util.stream.Stream
import kotlin.streams.toList

class GameOfLife(private val liveCells: Map<String, Cell>) {
    private val isReproducible: (Long) -> Boolean = { n -> n == 3L }
    private val isNextGeneration: (Long) -> Boolean = { n -> n in 2..3 }

    fun getLiveCells(): List<Cell> {
        return Optional.of(liveCells)
                .filter { it.isNotEmpty() }
                .map { it.values.toList() }
                .orElseThrow { RuntimeException("No more living cells") }
    }

    fun tick(): GameOfLife = GameOfLife(nextLiveCellsMap)

    private val nextLiveCellsMap: Map<String, Cell>
        get() = Stream.concat(nextGenerationCells, reproducibleCells)
                .map { (it.toString() to it) }
                .toList()
                .toMap()

    private val nextGenerationCells: Stream<Cell> get() = filteredCells(getLiveCells().stream(), isNextGeneration)

    private val reproducibleCells: Stream<Cell> get() = filteredCells(inactiveNeighbours, isReproducible)

    val inactiveNeighbours: Stream<Cell>
        get() = getLiveCells().stream()
                .flatMap { it.neighbours }
                .distinct()
                .filter { isNotActive(it) }

    private fun filteredCells(cells: Stream<Cell>, isActiveCell: (Long) -> Boolean) =
            cells.filter { isActiveCell(countActiveNeighbours(it)) }

    fun isActive(x: Int, y: Int) = isActive(toString(x, y))

    private fun isActive(key: String) = liveCells[key] != null

    private fun isNotActive(cell: Cell) = !isActive(cell.toString())

    private fun countActiveNeighbours(cell: Cell) = cell.neighbours.filter { c -> isActive(c.toString()) }.count()

}