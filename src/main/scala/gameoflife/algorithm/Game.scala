package gameoflife.algorithm

case class Game(allieveCells: List[ActiveCell]) {
  def tick(): Game = {
    Game(cellsToStay ++ cellsToBeGenerated)
  }

  def cellsToStay: List[ActiveCell] = {
    allieveCells.filter(isCellToStay)
  }

  def isCellToStay(cell: ActiveCell): Boolean = {
    val count = countActiveNeighbors(cell)
    count == 2 || count == 3
  }

  private def cellsToBeGenerated : List[ActiveCell] = {
    getInactiveNeighbours.filter(countActiveNeighbors(_) == 3)
  }

  private def getInactiveNeighbours : List[ActiveCell] = {
    allieveCells.flatMap(_.getNeighbours.toSet.filterNot(isLiveCell))
  }

  private def countActiveNeighbors(that: ActiveCell) : Int = {
    allieveCells.count(that.getNeighbours.contains(_))
  }

  private def isLiveCell(cell: ActiveCell): Boolean = {
    allieveCells.contains(cell)
  }

}