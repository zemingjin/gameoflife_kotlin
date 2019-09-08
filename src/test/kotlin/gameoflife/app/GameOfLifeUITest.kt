package gameoflife.app

import gameoflife.helper.*

import java.util.logging.Logger

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
        fun main(params: Array<String>) {
            val time = System.currentTimeMillis()
            GameOfLifeUITest(params).gameOfLifeUI.run()
            LOG.info("Total time: " + format(System.currentTimeMillis() - time))
        }
    }
}
