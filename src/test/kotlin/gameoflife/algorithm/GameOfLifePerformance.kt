package gameoflife.algorithm

import gameoflife.helper.buildGameOfLife
import gameoflife.helper.toTime
import kotlin.system.measureTimeMillis

object GameOfLifePerformance {
    private fun run(params: Array<String>) {
        println("Testing $DEF_TEST_PATH in $ITERATIONS times...")
        var gameOfLife = params.buildGameOfLife(DEF_TEST_PATH)

        println("Started...")
        measureTimeMillis { for (i in 1..ITERATIONS) gameOfLife = gameOfLife.tick() }
                .apply { println("Finished in $toTime.") }
    }

    @JvmStatic
    fun main(params: Array<String>) {
        run(params)
    }
}

private const val ITERATIONS = 500
private const val DEF_TEST_PATH = "src/main/resources/sidecar_gun.seed"