package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.models._
import com.razorfish.icfp_2015._

import scalaz.Scalaz._

class POPStrategy(val phrases: Set[PowerPhrase], encoder: MoveEncoder) extends Strategy {
  def initialState = ()

  val gameMovePhrases = MoveEncoder.phrasesOfPower.map(MoveEncoder.decodeMoves(_)).toSeq.sortBy(_.length).reverse

  var allowedGameMovePhrases: Seq[Seq[GameMove]] = Nil

  var phrase: Seq[GameMove] = Nil

  var phraseIndex = 0

  def incrementIndex = {
    phraseIndex = (phraseIndex + 1) % phrase.length
  }

  //val availableCr
  // "Ei!" => E, SW, W
  // "Ia! Ia! " => SW, SW, W, SE, SW, SW, W
  // "R'lyeh" => CW, W, SE, E, E, SW
  // "Yuggoth" => E, CCW, SW, SW, SE, CCW, SW

  // These problems have 0 score:
  // 1, 8, 9, 10, 13, 14, 15, 17, 19, 20, 21, 22, 23, 24

  def apply(board: Board, source: Source): EncodedMoves = {
    val initialConfiguration = GameConfiguration(board, source)
    var move: GameMove = E
    val moves = unfold((initialState, initialConfiguration)) {
      case (state, gc: ActiveGameConfiguration) =>

        if (gc.activeUnit.moveHistory.isEmpty) {
          //select best
          allowedGameMovePhrases = gameMovePhrases.filterNot(gc.activeUnit.unit.containsCycle(_))
          phrase = allowedGameMovePhrases
            .filterNot(m => gc.tryMoves(m).isEmpty)
            .headOption.getOrElse(Seq(move))
          phraseIndex = 0
        }
        if(phraseIndex == 0){

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

        Option((move, newGC), (incrementIndex, newGC))
      case (_, gc@CompletedGameConfiguration(_, _)) =>
        None
    }.map(_._1)
    encoder.encode(moves, phrases)
  }
}

