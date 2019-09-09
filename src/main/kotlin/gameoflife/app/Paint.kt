package gameoflife.app

import gameoflife.algorithm.GameOfLife
import java.awt.Graphics
import javax.swing.JPanel

class Paint(private val panel: JPanel) {
    var gridSize: Boundary? = null
    var cellSize: Int? = null
    var gameOfLife: GameOfLife? = null

     private val fillSize: Int get() = cellSize!! - 2

    private val fillCell: (Graphics) -> (Int) -> (Int) -> Unit = { graphics -> { y -> { x ->
        graphics.color = getColor(x, y)
        graphics.fillRect(getFillPosition(x), getFillPosition(y), fillSize, fillSize)
    }}}

    private val drawBorder: (Graphics) -> (Int) -> (Int) -> Unit = { graphics -> { y -> { x ->
        graphics.color = panel.foreground
        graphics.drawRect(getCellPosition(x), getCellPosition(y), cellSize!!, cellSize!!)
    }}}

    private val paints = arrayOf(fillCell, drawBorder)

    fun paint(graphics: Graphics) {
        paints.forEach { it(graphics) }
    }

    private fun getColor(x: Int, y: Int) = if (gameOfLife!!.isActive(x, y)) panel.foreground else panel.background
    private fun getFillPosition(index: Int) = getCellPosition(index) + 1
    private fun getCellPosition(index: Int) = index * cellSize!!

}