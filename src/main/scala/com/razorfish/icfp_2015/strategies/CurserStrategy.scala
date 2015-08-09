package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.{MoveEncoder, EncodedMoves}
import com.razorfish.icfp_2015.models.{ActiveGameConfiguration, GameMove, Source, Board}

/**
 * For each piece tries to find the longest sequence of moves (before frozen) using only sequences of power phrases
 */

/*
class CurserStrategy(encoder: MoveEncoder) extends SteppedEncodedStrategy[Seq[GameMove]] {

  def initialState = Seq.empty[GameMove]

  val decodeMove: Map[Char, GameMove] = {
    MoveEncoder.moveEncodings.flatMap {
      case (move, encodings) =>
        encodings.map(_ -> move)
    }
  }

  /**
   *
   * @param phrases
   * @return map of power phrases to their sequence of moves
   */
  def phraseMoves(phrases: Set[String]): Map[String, Seq[GameMove]] = {
    phrases.map { phrase =>
      phrase -> phrase.map(decodeMove)
    }.toMap
  }

  override def step(gc: ActiveGameConfiguration, state: Seq[GameMove]): (Char, Seq[GameMove]) = {

  }
}

*/