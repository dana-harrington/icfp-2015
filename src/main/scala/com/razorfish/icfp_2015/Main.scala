package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models._

object Main {
  def main(args: Array[String]): Unit = {
    println("Nothing so far")


    val board = new Board(10, 15, Set.empty)
    val testGameUnit = new GameUnit(Set(Cell(1,2), Cell(2,2), Cell(3,2), Cell(2,3)), Cell(1,2))
    board.print(testGameUnit)
  }
}
