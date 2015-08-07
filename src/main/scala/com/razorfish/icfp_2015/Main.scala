package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models._

object Main {
  def main(args: Array[String]): Unit = {
    val board = parseBoard
    val source = parseSource
    val phrases = parsePhrases

    val strategy = PhraseAfterthoughtStrategy(ReallyStupidAI, DumbEncoder)
    val encodedMoves = strategy(board, source, phrases)

    encodedMoves.moves.foreach(print)

  }
  val testGameUnit = GameUnit(
    members = Set(Cell(1,2), Cell(2,2), Cell(3,2), Cell(2,3)),
    pivot = Cell(1,2))

  def parseBoard: Board = Board(10, 15, Set.empty)
  def parseSource: Source = Seq(testGameUnit, testGameUnit).iterator
  def parsePhrases: Set[String] = Set("ie!")
}
