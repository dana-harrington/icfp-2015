package com.razorfish.icfp_2015.models

import org.specs2.mutable.Specification

class BoardTest extends Specification {

  "BoardTest" should {
    "filledRows" in {
      val filledRow = (0 to 10).map(Cell(_,7)).toSet
      val board = Board(10, 10, filledRow + Cell(3,6))
      board.filledRows === (1, Board(10, 10, Set(Cell(3,7))))
    }

  }
}
