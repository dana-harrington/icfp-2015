package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015._
import com.razorfish.icfp_2015.models._

import scala.annotation.tailrec

class POPBranchStrategy(val phrases: Set[PowerPhrase], encoder: MoveEncoder) extends Strategy {

  // "Ei!" => E, SW, W
  // "Ia! Ia! " => SW, SW, W, SE, SW, SW, W
  // "R'lyeh" => CW, W, SE, E, E, SW
  // "Yuggoth" => E, CCW, SW, SW, SE, CCW, SW
  val gameMovePhrases = phrases.map(MoveEncoder.decodeMoves(_)).toSeq.sortBy(_.length).reverse

  private val defaultMove = SE

  @tailrec
  private def rec(
                   gc: ActiveGameConfiguration,
                   moves: Seq[GameMove],
                   //all valid POP for active unit
                   _allowedGameMovePhrases: Seq[Seq[GameMove]],
                   //current POP we are working to achieve
                   _phrase: Seq[GameMove],
                   //current index in POP
                   _phraseIndex: Int
                   ): (Score, EncodedMoves) = {

    var allowedGameMovePhrases = _allowedGameMovePhrases
    var phrase = _phrase
    var phraseIndex = _phraseIndex

    /// all moves that will not create cycle
    if (gc.activeUnit.moveHistory.isEmpty) allowedGameMovePhrases = gameMovePhrases.filterNot(gc.activeUnit.unit.containsCycle)
    /// If we don't have a current partially completed phrase pick a new phrase if possible
    if (phraseIndex == 0) {
      //select best
      val phrases = allowedGameMovePhrases
        .filterNot(m => gc.tryMoves(m).isEmpty)

      phrase = if (phrases.isEmpty) Seq(defaultMove) else phrases.head
      phraseIndex = 0
    }

    val nextMoveInPhrase = phrase(phraseIndex)

    val nextMove = if (gc.activeUnit.unit.containsCycle(gc.activeUnit.moveHistory :+ nextMoveInPhrase)) {
      //start a new phrase
      phrase = allowedGameMovePhrases
        .filterNot(p => gc.activeUnit.unit.containsCycle(gc.activeUnit.moveHistory ++ p))
        .filterNot(m => gc.tryMoves(m).isEmpty)
        .headOption.getOrElse(Seq(defaultMove))
      phraseIndex = 0
      phrase.head
    } else {
      nextMoveInPhrase
    }
    phraseIndex = (phraseIndex + 1) % phrase.length

    gc.update(nextMove) match {
      case ac: ActiveGameConfiguration =>
        rec(ac, moves :+ nextMove, allowedGameMovePhrases, phrase, phraseIndex)
      case dc: CompletedGameConfiguration =>
        val encodedMoves = encoder.encode(moves, phrases)
        (dc.score, encodedMoves)
    }

  }

  def apply(board: Board, source: Source): EncodedMoves = {
    val initialConfiguration = GameConfiguration(board, source)
    initialConfiguration match {
      case ac: ActiveGameConfiguration => rec(ac, Nil, Nil, Nil, 0)._2
      case dc: CompletedGameConfiguration => encoder.encode(Nil, phrases)
    }
  }
}

