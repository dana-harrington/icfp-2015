package com.razorfish.icfp_2015.models

case class Board(width: Int, height: Int, filledCells: Set[Cell]) {

  def tileState(p: Cell): TileState =
    if (filledCells.contains(p)) FilledTile
    else if (p.row >= 0 && p.row < height && p.column >= 0 && p.column < width) EmptyTile
    else InvalidTile
  
  def cellState(c: Cell): CellState = {
    tileState(c) match {
      case cs: CellState => cs
      case InvalidTile => throw new Exception(s"Invalid board tile $c")
    }
  }

  def freeze(unit: GameUnit): Board = {
    this.copy(filledCells = filledCells ++ unit.positions)
  }

  def print(gameUnit: GameUnit = NilGameUnit) = {

    println(" " + ("_" * (width * 2 + 2)))

    for (h <- 0 until height) {
      val open = if (h % 2 == 0) "<" else " >"
      var line = ""

      for (w <- 0 until width) {

        val cell = Cell(w, h)

        val unitState = gameUnit.tileState(cell)
        unitState match {
          case FilledTile => line += "()"

          case EmptyTile =>

            val state = cellState(cell)
            state match {
              case FilledTile => line += "[]"
              case EmptyTile => line += "  "
            }

          case InvalidTile => throw new Exception()
        }

      }

      val close = if (h % 2 == 0) "<" else ">"
      println(open + line + close)
    }

    val fclose = if (height % 2 == 0) " " else "  "
    println(fclose + ("-" * (width * 2 + 2)))

  }
  
  def filledRows: (Int, Board) = {
    var board = this
    var rowsFilled = 0
    for (row <- (height-1) to 0 by -1) {
      val rowState = for (col <- 0 until width) yield {
        cellState(Cell(col, row))
      }
      if (rowState.forall(_ == FilledTile)) {
        board = board.shiftDown(row)
        rowsFilled += 1
      }
    }
    (rowsFilled, board)
  }

  def shiftDown(toRow: Int) = {
    copy(filledCells = filledCells.filter(_.row != toRow).map { filledCell =>
      if (filledCell.row < toRow) {
        filledCell.copy(row = filledCell.row + 1)
      } else {
        filledCell
      }
    })
  }
}
