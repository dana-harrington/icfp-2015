package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models._
import scalaz.Scalaz._

object Main {
  def main(args: Array[String]): Unit = {
    val initialBoard = parseBoard
    val source = parseSource
    val initialConfiguration = GameConfiguration(initialBoard, source)
    val ai: AI = ReallyStupidAI
    val moveEncoder: MoveEncoder = DumbEncoder
    val moves: Stream[(GameMove, GameConfiguration)] = unfold(initialConfiguration){
      case gc@GameConfigurationImpl(_, _, _, _, _) =>
        val move = ai.step(gc)
        val newGC = gc.update(move)
        Some((move, newGC), newGC)
      case gc@GameDoneConfiguration(_,_) =>
        None
    }

    val encodedMoves = moveEncoder.encode(moves.map(_._1))

    println(encodedMoves.moves)


    val board = new Board(10, 15, Set.empty)
    val testGameUnit = new GameUnit(Set(Cell(1,2), Cell(2,2), Cell(3,2), Cell(2,3)), Cell(1,2))
    board.print(testGameUnit)
  }

  def parseBoard: Board = ???
  def parseSource: Source = ???
}
