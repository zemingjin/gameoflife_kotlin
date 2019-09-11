package gameoflife.app

import gameoflife.algorithm.GameOfLife
import gameoflife.app.GameOfLifeUI.Companion.MAX_CELL_SIZE
import java.awt.Graphics
import java.util.stream.IntStream
import javax.swing.JPanel

class Paint(private val panel: JPanel) {
    var cellSize: Int? = MAX_CELL_SIZE
    var gameOfLife: GameOfLife? = null
    var boundary: Boundary? = null

    private val fillSize: Int get() = cellSize!! - 2

    private val fillCell: (Graphics) -> (Int) -> (Int) -> Unit = { graphics -> { y -> { x ->
        graphics.color = getColor(x, y)
        graphics.fillRect(getFillPosition(x), getFillPosition(y), fillSize, fillSize)
    }}}

    private val drawBorder: (Graphics) -> (Int) -> (Int) -> Unit = { graphics -> { y -> { x ->
        graphics.color = panel.foreground
        graphics.drawRect(getCellPosition(x), getCellPosition(y), cellSize!!, cellSize!!)
    }}}

    private val paints = listOf(fillCell, drawBorder)

    private fun getCellPosition(index: Int) = index * cellSize!!

    private fun getFillPosition(index: Int) = getCellPosition(index) + 1

    private fun getColor(x: Int, y: Int) = if (gameOfLife!!.isActive(x, y)) panel.foreground else panel.background

    private fun paintRow(actor: (Int) -> Unit) { IntStream.range(0, boundary!!.x).forEach { actor(it) } }

    private fun paintRows(paint: (Int) -> (Int) -> Unit) = (0 until boundary!!.y).forEach { paintRow(paint(it)) }

    fun paint(graphics: Graphics) { paints.forEach { paintRows(it(graphics)) } }
}