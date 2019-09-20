package gameoflife.algorithm

import gameoflife.helper.buildGameOfLife
import gameoflife.helper.toTime

object GameOfLifePerformance {
    private fun run(params: Array<String>) {
        println("Testing $DEF_TEST_PATH in $ITERATIONS times...")
        var gameOfLife = params.buildGameOfLife(DEF_TEST_PATH)

        println("Started...")
        val time = System.currentTimeMillis()
        (0 until ITERATIONS).forEach { _ -> gameOfLife = gameOfLife.tick() }
        println((System.currentTimeMillis() - time).toTime)
    }

    @JvmStatic
    fun main(params: Array<String>) {
        run(params)
    }
}

private const val ITERATIONS = 500
private const val DEF_TEST_PATH = "src/main/resources/sidecar_gun.seed"

