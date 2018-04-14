package gameoflife.algorithm

case class ActiveCell(x: Int, y: Int) {
  def getNeighbours: List[ActiveCell] = {
    getNeighboursInRow(y - 1) ++ getNeighboursInMiddleRow ++ getNeighboursInRow(y + 1)
  }

  def getNeighboursInRow(row: Int): List[ActiveCell] = {
    List(ActiveCell(x - 1, row),
         ActiveCell(x, row),
         ActiveCell(x + 1, row))
  }

  def getNeighboursInMiddleRow: List[ActiveCell] = {
    List(ActiveCell(x - 1, y), ActiveCell(x + 1, y))
  }

  def equals(that: ActiveCell): Boolean = {
    this.x == that.x && this.y == that.y
  }

}
