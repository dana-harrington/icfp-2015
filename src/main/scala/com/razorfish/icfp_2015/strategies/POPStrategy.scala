package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.MoveEncoder.PowerWord
import com.razorfish.icfp_2015.models._
import com.razorfish.icfp_2015.{EncodedMoves, MoveEncoder}

import scalaz.Scalaz._

class POPStrategy extends Strategy {
  def initialState = ()

  // "Ei!" => E, SW, W
  // "Ia! Ia! " => SW, SW, W, SE, SW, SW, W
  // "R'lyeh" => CW, W, SE, E, E, SW
  // "Yuggoth" => E, CCW, SW, SW, SE, CCW, SW

  // 1, 8, 9, 10, 13, 14, 15, 17, 19, 20, 21, 22, 23, 24

  def apply(board: Board, source: Source, phrases: Set[String]): EncodedMoves = {
    val initialConfiguration = GameConfiguration(board, source)
    var move: GameMove = SE
    val moves = unfold((initialState, initialConfiguration)) {
      case (state, gc@ActiveGameConfiguration(_, _, _, _, _)) =>

        move = move match {
          case W => if (gc.activeUnit.history.isEmpty) E else SE
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
    SimpleMoveEncoder.encode(moves, phrases.toSeq.map(_.toVector))
  }

  private object SimpleMoveEncoder extends MoveEncoder {
    def encode(moves: Seq[GameMove], powerWords: Seq[PowerWord]): EncodedMoves = {
      val solution: String = String.copyValueOf(moves.flatMap {
        case W => "!"
        case SW => "i"
        case SE => " "
        case E => "e"
        case CW => "d"
        case CCW => "k"
      }.toArray)
      val powerWordScore = phraseOfPowerScore("ei!", "ei!".r.findAllMatchIn(solution).length)
      EncodedMoves(solution, powerWordScore)
    }

    def phraseOfPowerScore(phraseOfPower: String, occurances: Int) = {
      val lenp = phraseOfPower.length
      val repsp = occurances
      val power_bonusp = if (occurances > 0) 300 else 0

      2 * lenp * repsp + power_bonusp
    }
  }

}

