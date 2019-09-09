package gameoflife.helper

import gameoflife.app.Boundary
import gameoflife.algorithm.Cell

import java.util.Optional
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.streams.toList

class SeedHelper {

    /**
     * This method is used only by tests
     * @param seeds the given seeds in the format of "1|1, 1|2, 1|3"
     * @return self
     */
    fun seedToMap(seeds: String): Map<String, Cell> =
            Optional.ofNullable(seeds)
                .filter { it.isNotEmpty() }
                .map { seedLiveCells(it) }
                .orElseThrow { RuntimeException(String.format("Invalid seeds: '%s'", seeds)) }

    private fun seedLiveCells(liveCells: String) =
            Stream.of(*liveCells.split(CELL_DELIMITER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                .map { getCellFromString(it) { x, y -> Cell(x, y)} }
                .map { (it.toString() to it) }
                .toList()
                .toMap()

    private fun <T : Cell> getCellFromString(values: String, construct: (Int, Int) -> T): T {
        val indices = values.split(INDICES_DELIMITER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return construct(indices[0].trim { it <= ' ' }.toInt(), indices[1].trim { it <= ' ' }.toInt())
    }

    /**
     * @param seeds the first line contains the size info, such as "#P width|height".
     * the rest is in the format of ".....OO.O" where the capital 'O' indicate live cell(s).
     * @return this
     */
    fun seedToMap(seeds: Array<String>) = seedLiveCells(seeds.copyOfRange(1, seeds.size))

    private fun getBoundary(seed: String) = getCellFromString(seed) { x, y -> Boundary(x, y) }

    fun getBoundaryFromHeader(seed: Array<String>) =
            getBoundary(seed[0].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])

    private fun seedLiveCells(seeds: Array<String>) =
            IntStream.range(0, seeds.size)
                .mapToObj { y -> getLiveCellsFromRow(seeds[y], y) }
                .flatMap { stream -> stream }
                .map { (it.toString() to it) }
                .toList()
                .toMap()

    private fun getLiveCellsFromRow(line: String, y: Int) =
            IntStream.range(0, line.length)
                .filter { x -> isLiveCell(line[x]) }
                .mapToObj { x -> Cell(x, y) }

    private fun isLiveCell(c: Char) = c == LIVE_CELL

    companion object {
        private const val INDICES_DELIMITER = "\\|"
        private const val CELL_DELIMITER = ", "
        private const val LIVE_CELL = 'O'
    }
}
