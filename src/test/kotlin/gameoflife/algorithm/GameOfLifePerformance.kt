package gameoflife.algorithm

import gameoflife.helper.buildGameOfLife

class GameOfLifePerformance private constructor() {

    private fun run(params: Array<String>) {
        var gameOfLife = params.buildGameOfLife(DEF_TEST_PATH)
        val time = System.currentTimeMillis()

        println("Started...")
        for (i in 0 until ITERATIONS) { gameOfLife = gameOfLife.tick() }
        println(format(System.currentTimeMillis() - time))
    }

    companion object {
        private const val ITERATIONS = 500
        private const val DEF_TEST_PATH = "src/main/resources/sidecar_gun.seed"

        @JvmStatic
        fun main(params: Array<String>) {
            GameOfLifePerformance().run(params)
        }

        private fun format(time: Long): String {
            return "Finished in %tM:%tS.%tL".format(time, time, time)
        }
    }
}
