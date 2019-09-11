package gameoflife.app

import gameoflife.algorithm.GameOfLife
import gameoflife.helper.*
import gameoflife.helper.SeedHelper

import javax.swing.JFrame
import javax.swing.WindowConstants
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Insets
import java.awt.KeyEventPostProcessor
import java.awt.KeyboardFocusManager
import java.awt.Toolkit
import java.awt.event.KeyEvent
import javax.swing.JPanel

class GameOfLifeUI(params: Array<String>) : JPanel(), KeyEventPostProcessor {
    var isContinue = object : ContinueCheck { override fun isContinue() = isContinueFlag }
    private val gameOfLife get() = paint.gameOfLife

    private val window = JFrame()
    private var isContinueFlag = true
    private var evolveToggle = 1
    private var automaton = true
    private var path: String? = null
    private var iteration: Int = 0
    private var waitTime: Int = 0
    private val screenSize: Dimension get() = Toolkit.getDefaultToolkit().screenSize
    private val isContinueToEvolve get() = automaton || evolveToggle == 0
    private val boundary get() = paint.boundary
    private val paint = Paint(this)

    init {
        params.takeIf { it.isNotEmpty() }
                .let { this.setup(it!!) }
    }

    private fun setup(params: Array<String>): GameOfLifeUI {
        path = params[0]
        paint.gameOfLife = buildGameOfLife(loadSeeds(path!!))
        paint.gameOfLife = gameOfLife
        automaton = params.isAutomaton
        waitTime = params.waitTime
        return this
    }

    private fun buildGameOfLife(seeds: Array<String>): GameOfLife {
        paint.boundary = SeedHelper.getBoundaryFromHeader(seeds)
        return GameOfLife(SeedHelper.seedToMap(seeds))
    }

    fun setWaitTime(waitTime: Int): GameOfLifeUI {
        this.waitTime = waitTime
        return this
    }

    fun run() {
        setupKeyboardListener()
        setupFrame()

        while (isContinue.isContinue()) {
            repaint()
            waitAWhile()
            if (isContinueToEvolve) {
                evolve()
            }
        }
        close()
    }

    private fun close() {
        window.isVisible = false
        window.dispose()
    }

    private fun setupKeyboardListener() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(this)
    }

    private fun setupFrame() {
        paint.cellSize = calculateCellSize

        val width = calculatePanelSize(boundary!!.x.toDouble())
        val height = calculatePanelSize(boundary!!.y.toDouble())

        setSize(width, height)
        isFocusable = true
        window.title = getTitle(path)
        window.isResizable = false
        window.isVisible = true
        window.setBounds(getHorizontalPosition(width), getVerticalPosition(height),
                getFrameWidth(width), getFrameHeight(height))
        window.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        window.contentPane.add(this)
    }

    private val calculateCellSize: Int get() {
        val screenSize = screenSize
        return calculateCellSize(screenSize.height, boundary!!.y.toDouble())
                .coerceAtMost(calculateCellSize(screenSize.width, boundary!!.x.toDouble()))
                .coerceAtMost(MAX_CELL_SIZE)
                .coerceAtLeast(MIN_CELL_SIZE)
    }

    private fun calculateCellSize(screenSize: Int, numberOfCells: Double) =
            ((screenSize * 3).toDouble() / 4.0 / numberOfCells).toInt()

    private fun calculatePanelSize(position: Double) = (position * cellSize!!).toInt()

    private fun getHorizontalPosition(panelWidth: Int) = calculatePosition(screenSize.width, getFrameWidth(panelWidth))

    private fun getVerticalPosition(panelHeight: Int) = calculatePosition(screenSize.height, getFrameHeight(panelHeight))

    private fun calculatePosition(screenSize: Int, frameSize: Int) = (screenSize - frameSize) / 2

    private fun getFrameWidth(panelWidth: Int) = panelWidth + calculateInsertsValue { it.left + it.right }

    private fun getFrameHeight(panelHeight: Int) = panelHeight + calculateInsertsValue { it.top + it.bottom }

    private fun calculateInsertsValue(calculate: (Insets) -> Int) = calculate(window.insets)

    private fun evolve() {
        paint.gameOfLife = gameOfLife!!.tick()
        evolveToggle++
        window.title = "$path - #$iteration"
        iteration++
    }

    private val cellSize get() = paint.cellSize
    override fun paint(graphics: Graphics) { paint.paint(graphics) }

    @Synchronized
    private fun waitAWhile() {
        if (waitTime > 0) {
              Thread.sleep(waitTime.toLong())
        }
    }

    override fun postProcessKeyEvent(e: KeyEvent): Boolean {
        var result = false
        if (e.keyCode == KeyEvent.VK_ESCAPE) {
            isContinueFlag = false
        } else if (e.keyCode == KeyEvent.VK_SPACE) {
            automaton = e.isControlDown
            evolveToggle = if (evolveToggle < 2) evolveToggle + 1 else 0
            e.consume()
            result = true
        }
        return result
    }

    private fun getTitle(path: String?) = "Seed: $path"

    companion object {
        const val MAX_CELL_SIZE = 100
        private const val MIN_CELL_SIZE = 6

        @JvmStatic
        fun main(params: Array<String>) {
            GameOfLifeUI(params).run()
        }
    }
}