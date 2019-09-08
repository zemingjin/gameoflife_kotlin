package gameoflife.app

import gameoflife.algorithm.Cell

class Boundary(x: Int, y: Int) : Cell(x, y) {

    fun isInBound(that: Cell) = checkBoundary(that.x, x) && checkBoundary(that.y, y)

    private fun checkBoundary(value: Int, boundary: Int) = value in 0 until boundary
}

