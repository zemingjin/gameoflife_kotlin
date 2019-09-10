package gameoflife.helper

import gameoflife.algorithm.GameOfLife
import java.lang.RuntimeException
import java.util.*

fun Array<String>.buildGameOfLife(defPath: String? = null): GameOfLife =
        Optional.ofNullable(testPath(defPath))
                .map { loadSeeds(it) }
                .map { SeedHelper().seedToMap(it) }
                .map { GameOfLife(it) }
                .orElseThrow { throw RuntimeException("Invalid input source!")}

private fun Array<String>.testPath(defPath: String?): String =
    Optional.of(this)
            .filter { it.isNotEmpty() }
            .map { it[0] }
            .orElse(defPath)