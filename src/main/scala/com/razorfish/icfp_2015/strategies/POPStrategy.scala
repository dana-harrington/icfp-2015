package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.models._
import com.razorfish.icfp_2015._

import scalaz.Scalaz._

class POPStrategy(val phrases: Set[PowerPhrase], encoder: MoveEncoder) extends Strategy {

  // "Ei!" => E, SW, W
  // "Ia! Ia! " => SW, SW, W, SE, SW, SW, W
  // "R'lyeh" => CW, W, SE, E, E, SW
  // "Yuggoth" => E, CCW, SW, SW, SE, CCW, SW
  val gameMovePhrases = MoveEncoder.phrasesOfPower.map(MoveEncoder.decodeMoves(_)).toSeq.sortBy(_.length).reverse

  var allowedGameMovePhrases: Seq[Seq[GameMove]] = Nil

  var phrase: Seq[GameMove] = Nil

  var phraseIndex = 0

  def incrementIndex() = {
    phraseIndex = (phraseIndex + 1) % phrase.length
  }

  def apply(board: Board, source: Source): EncodedMoves = {
    val initialConfiguration = GameConfiguration(board, source)
    var move: GameMove = E
    val moves = unfold(initialConfiguration) {
      case gc: ActiveGameConfiguration =>

        if (gc.activeUnit.moveHistory.isEmpty) {
          //select best
          allowedGameMovePhrases = gameMovePhrases.filterNot(gc.activeUnit.unit.containsCycle)
          phrase = allowedGameMovePhrases
            .filterNot(m => gc.tryMoves(m).isEmpty)
            .headOption.getOrElse(Seq(move))
          phraseIndex = 0
        }
        val newMove = phrase(phraseIndex)
        if (gc.activeUnit.unit.containsCycle(gc.activeUnit.moveHistory :+ newMove)) {
          //start a new phrase
          phrase = allowedGameMovePhrases
            .filterNot(p => gc.activeUnit.unit.containsCycle(gc.activeUnit.moveHistory ++ p))
            .filterNot(m => gc.tryMoves(m).isEmpty)
            .headOption.getOrElse(Seq(move))
          phraseIndex = 0
          move = phrase.head
        }else {
          move = newMove
        }
        val newGC = gc.update(move)
        incrementIndex()
        Option((move, newGC), newGC)
      case gc@CompletedGameConfiguration(_, _) =>
        None
    }.map(_._1)
    encoder.encode(moves, phrases)
  }
}

