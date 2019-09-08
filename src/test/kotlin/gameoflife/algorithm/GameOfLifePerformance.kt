package gameoflife.algorithm

import gameoflife.app.GameOfLifeUI

import java.util.Optional

class GameOfLifePerformance private constructor() {

    private fun run(params: Array<String>) {

        var gameOfLife = GameOfLifeUI(checkPath(params)).setWaitTime(0).gameOfLife
        val time = System.currentTimeMillis()

        println("Started...")
        for (i in 0 until ITERATIONS) {
            gameOfLife = gameOfLife?.tick()
        }
        println(format(System.currentTimeMillis() - time))
    }

    private fun checkPath(params: Array<String>): Array<String> {
        return Optional.ofNullable(params)
                .filter { it.isNotEmpty() }
                .orElseGet { arrayOf("src/main/resources/sidecar_gun.seed") }
    }

    companion object {
        private const val ITERATIONS = 500

        @JvmStatic
        fun main(params: Array<String>) {
            GameOfLifePerformance().run(params)
        }

        private fun format(time: Long): String {
            return "Finished in %tM:%tS.%tL".format(time, time, time)
        }
    }
}
