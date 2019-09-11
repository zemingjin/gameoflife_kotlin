package gameoflife.helper

import gameoflife.algorithm.GameOfLife
import java.lang.RuntimeException

fun Array<String>.buildGameOfLife(defPath: String? = null): GameOfLife {
    return testPath(defPath)
            ?.let { loadSeeds(it) }
            ?.let { SeedHelper.seedToMap(it) }
            ?.let { GameOfLife(it) }
            ?: throw RuntimeException("Invalid input source!")
}

private fun Array<String>.testPath(defPath: String?): String? {
    return takeIf { it.isNotEmpty() }
            .let { it?.get(0) }
            ?: defPath
}