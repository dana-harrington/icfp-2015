package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.{PowerPhrase, MoveEncoder, EncodedMoves}
import com.razorfish.icfp_2015.models._

import scala.util.Random

/**
 * For each piece tries to find the longest sequence of moves (before frozen) using only sequences of power phrases
 */


class CurserStrategy(val phrases: Set[PowerPhrase]) extends SteppedEncodedStrategy[Seq[Char]] {

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
  def phraseMoves(phrases: Set[String]): Map[PowerPhrase, Seq[GameMove]] = {
    phrases.map { phrase =>
      phrase -> phrase.map(decodeMove)
    }.toMap
  }

  override def step(gc: ActiveGameConfiguration, state: Seq[Char]): (Char, Seq[Char]) = {
    state match {
      case move +: moves =>
        (move, moves)
      case Seq() =>
        val unitMoves = solveForUnit(gc)
        step(gc, unitMoves)
    }
  }

  // Use phrase of power if we can, otherwise go SW or SE
  def solveForUnit(gc: ActiveGameConfiguration): Seq[Char] = {
    val phrasePaths = phrases.map { phrase =>
      (phrase, phrase.map(MoveEncoder.decodeMove))
    }
    val usablePhrases = for {
      (phrase, phrasePath) <- phrasePaths
      if !gc.activeUnit.containsCycle(phrasePath)
      if gc.tryMoves(phrasePath).isDefined // Does not freeze piece (or end game)
    } yield {
      phrase
    }
    // Only consider score of occurrence in isolation for simplicity
    val scoredPhrases = usablePhrases.map(ph => (ph, PowerPhrase.phraseOfPowerScore(ph, 1)))
    if (scoredPhrases.nonEmpty) {
      scoredPhrases.maxBy(_._2)._1
    } else {
      if (Random.nextBoolean()) {
        Seq(MoveEncoder.moveEncodings(SW).head)
      } else {
        Seq(MoveEncoder.moveEncodings(SE).head)
      }
    }
  }
}
