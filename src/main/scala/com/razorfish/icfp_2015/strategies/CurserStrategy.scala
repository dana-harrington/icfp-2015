package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.{MoveEncoder, EncodedMoves}
import com.razorfish.icfp_2015.models.{ActiveGameConfiguration, GameMove, Source, Board}

/**
 * For each piece tries to find the longest sequence of moves (before frozen) using only sequences of power phrases
 */


class CurserStrategy(encoder: MoveEncoder) extends SteppedEncodedStrategy[Seq[Char]] {

  def initialState = Seq.empty[Char]

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

  override def step(gc: ActiveGameConfiguration, state: Seq[Char], phrases: Set[String]): (Char, Seq[Char]) = {
    state match {
      case move +: moves =>
        (move, moves)
      case Seq() =>
        val unitMoves = solveForUnit(gc, phrases)
        step(gc, unitMoves, phrases)
    }
  }

  def solveForUnit(gc: ActiveGameConfiguration, phrases: Set[String]): Seq[Char] = {
    val phrasePaths = phrases.map { phrase =>
      phrase.map(MoveEncoder.decodeMove)
    }
    ??? // WIP

  }
}
