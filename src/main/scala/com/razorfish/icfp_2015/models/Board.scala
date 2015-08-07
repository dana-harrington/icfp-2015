package com.razorfish.icfp_2015.models

class Board(val width: Int, val height: Int, val positions: Seq[Position]) {

  def tileState(p: Cell): CellState =
    if (positions.contains(p)) FilledTile
    else EmptyTile


  def print(gameUnit: GameUnit = NilGameUnit) = {

    println(" " + ("_" * (width * 2 + 2)))

    for (h <- 0 to height) {
      val open = if (h % 2 == 0) "<" else " >"
      var line = ""

      for (w <- 0 to width) {

        val cell = Cell(w, h)

        val unitState = gameUnit.tileState(cell)
        unitState match {
          case FilledTile => line += "()"

          case EmptyTile =>

            val state = tileState(cell)
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


}
