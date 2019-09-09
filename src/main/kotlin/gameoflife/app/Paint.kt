package gameoflife.app

import gameoflife.algorithm.GameOfLife
import java.awt.Color
import java.awt.Graphics
import java.util.*
import javax.swing.JPanel

class Paint(private val panel: JPanel, private val gridSize: Boundary, private val cellSize: Int) {
    private var gameOfLife: GameOfLife? = null

    private val fillSize get() = cellSize - 2

    private val fillCell: (Graphics) -> (Int) -> (Int) -> Unit = { graphics -> { y -> { x ->
        graphics.color = getColor(x, y)
        graphics.fillRect(fillPosition(x), fillPosition(y), fillSize, fillSize)
    }}}

    private val drawBorder: (Graphics) -> (Int) -> (Int) -> Unit = { graphics -> { y -> { x ->
        graphics.color = panel.foreground
        graphics.drawRect(cellPosition(x), cellPosition(y), cellSize, cellSize)
    }}}

    private val paints = arrayOf(fillCell, drawBorder)

    private fun paintRow(f: (Int) -> Unit) { (0 until gridSize.x).forEach { f(it) } }

    private fun paintRows(f: (Int) -> (Int) -> Unit) { (0 until gridSize.y).forEach { paintRow(f(it)) } }

    fun paint(graphics: Graphics) { paints.forEach { paintRows(it(graphics)) } }

    fun setGameOfLife(gameOfLife: GameOfLife?): Paint {
        this.gameOfLife = gameOfLife
        return this
    }

    private fun cellPosition(index: Int): Int = index * cellSize

    private fun fillPosition(index: Int): Int = cellPosition(index) + 1

    private fun getColor(x: Int, y: Int): Color = if (isActiveCell(x, y)) panel.foreground else panel.background

    private fun isActiveCell(x: Int, y: Int): Boolean =
            Optional.ofNullable(gameOfLife).map { it.isActive(x, y) } .orElse(false)
}