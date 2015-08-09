package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.{PowerPhrase, MoveEncoder, EncodedMoves}
import com.razorfish.icfp_2015.models._

import scalaz.Scalaz._

class EiStrategy(val phrases: Set[PowerPhrase], encoder: MoveEncoder) extends Strategy {
  def initialState = ()

  // "Ei! " => E, SW, W, SE

  def apply(board: Board, source: Source): EncodedMoves = {
    val initialConfiguration = GameConfiguration(board, source)
    var move: GameMove = SE
    val moves = unfold((initialState, initialConfiguration)) {
      case (state, gc: ActiveGameConfiguration) =>

        move = move match {
          case W => if (gc.activeUnit.moveHistory.isEmpty) E else SE
          case SE => E
          case E => SW
          case SW => W
          case _ => throw new Exception("Strategy is too dumb to move like this")
        }

        val newGC = gc.update(move)

        Option((move, newGC), (initialState, newGC))
      case (_, gc@CompletedGameConfiguration(_, _)) =>
        None
    }.map(_._1)
    encoder.encode(moves, phrases)
  }
}

