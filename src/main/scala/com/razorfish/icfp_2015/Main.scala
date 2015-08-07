package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models._

object Main {
  def main(args: Array[String]): Unit = {
    println("Nothing so far")


    val board = new BoardImpl(10, 15, Nil)
    val testGameUnit = new GameUnitImpl(Seq(Position(1,2), Position(2,2), Position(3,2), Position(2,3)), Position(1,2))
    board.print(testGameUnit)
  }
}

object AI {
  def step(gc: GameConfiguration): GameMove = ???
}
