package gameoflife.algorithm

import gameoflife.helper.buildGameOfLife

class GameOfLifePerformance private constructor() {

    private fun run(params: Array<String>) {
        println("Testing $DEF_TEST_PATH in $ITERATIONS times...")
        var gameOfLife = params.buildGameOfLife(DEF_TEST_PATH)

        println("Started...")
        val time = System.currentTimeMillis()
        for (i in 0 until ITERATIONS) { gameOfLife = gameOfLife.tick() }
        println((System.currentTimeMillis() - time).format)
    }

    companion object {
        @JvmStatic
        fun main(params: Array<String>) {
            GameOfLifePerformance().run(params)
        }
    }
}
private const val ITERATIONS = 500
private const val DEF_TEST_PATH = "src/main/resources/sidecar_gun.seed"

val Long.format: String get() = "Finished in %tM:%tS.%tL".format(this, this, this)
