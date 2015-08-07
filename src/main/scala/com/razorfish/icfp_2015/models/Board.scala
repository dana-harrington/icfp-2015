package com.razorfish.icfp_2015.models

case class Board(width: Int, height: Int, filledCells: Set[Cell]) {

  def tileState(p: Cell): TileState =
    if (filledCells.contains(p)) FilledTile
    else if (p.row >= 0 && p.row < height && p.column >= 0 && p.column < width) EmptyTile
    else InvalidTile

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
          case FilledTile => line += "◀▶"

          case EmptyTile =>

            val state = tileState(cell)
            state match {
              case FilledTile => line += "[]"
              case EmptyTile => line += "  "
              case InvalidTile => throw new Exception("What the hell!? Tried to print a cell outside of the board")
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
}
