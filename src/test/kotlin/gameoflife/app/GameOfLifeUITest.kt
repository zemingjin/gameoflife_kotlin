package gameoflife.app

import gameoflife.helper.*

import java.util.logging.Logger
import kotlin.system.measureTimeMillis

internal class GameOfLifeUITest private constructor(params: Array<String>) : ContinueCheck {

    private var iterations = ITERATIONS
    private val gameOfLifeUI: GameOfLifeUI = GameOfLifeUI(params)

    init {
        gameOfLifeUI.setWaitTime(50)
        gameOfLifeUI.isContinue = this
    }

    override fun isContinue(): Boolean {
        return iterations-- >= 0
    }

    companion object {
        private const val ITERATIONS = 500
        private val LOG = Logger.getLogger(GameOfLifeUITest::class.java.name)

        @JvmStatic
        suspend fun main(params: Array<String>) {
            measureTimeMillis { GameOfLifeUITest(params).gameOfLifeUI.run() }
                .apply { LOG.info("Total time: $toTime") }
        }
    }
}
