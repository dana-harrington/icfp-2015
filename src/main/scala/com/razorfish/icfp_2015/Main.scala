package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models.{Source, GameMove, GameConfiguration}

object Main {
  def main(args: Array[String]): Unit = {
    println("Nothing so far")
  }
}

trait AI {
  def step(gc: GameConfiguration, source: Source): GameMove = ???
}

case class EncodedMoves(moves: Seq[Char], powerWordScore: Score)

trait MoveEncoder {
  def encode(moves: Seq[GameMove]): EncodedMoves
}