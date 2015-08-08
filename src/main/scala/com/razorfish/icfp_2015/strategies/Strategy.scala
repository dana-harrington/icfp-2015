package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.EncodedMoves
import com.razorfish.icfp_2015.models._

import scalaz.Scalaz._

trait Strategy {
  def apply(board: Board, source: Source, phrases: Set[String]): EncodedMoves
}

/**
 * Considers only the game, not the phrases to pick moves. Then attempts to optimize the encoding
 * @param ai
 * @param moveEncoder
 */
case class PhraseAfterthoughtStrategy(ai: MoveAI, moveEncoder: MoveEncoder) extends Strategy {
  def apply(board: Board, source: Source, phrases: Set[String]): EncodedMoves = {
    val initialConfiguration = GameConfiguration(board, source)
    val moves: Stream[(GameMove, GameConfiguration)] = unfold(initialConfiguration){
      case gc@GameConfigurationImpl(_, _, _, _, _) =>
        val move = ai.step(gc)
        val newGC = gc.update(move)
        Some((move, newGC), newGC)
      case gc@GameDoneConfiguration(_,_) =>
        None
    }
    moveEncoder.encode(moves.map(_._1))
  }
}
