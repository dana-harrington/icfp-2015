package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.{MoveEncoder, EncodedMoves}
import com.razorfish.icfp_2015.models.{GameMove, Source, Board}

/**
 * For each piece tries to find the longest sequence of moves (before frozen) using only sequences of power phrases
 */
object CurserStrategy extends Strategy {

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
  def phraseMoves(phrases: Seq[Vector[Char]]): Map[Vector[Char], Seq[GameMove]] = {
    phrases.map { phrase =>
      phrase -> phrase.map(decodeMove)
    }.toMap
  }

  def apply(board: Board, source: Source, phrases: Set[String]): EncodedMoves = ???

}
