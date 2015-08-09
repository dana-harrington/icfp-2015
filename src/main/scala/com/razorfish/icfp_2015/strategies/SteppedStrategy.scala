package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.{PowerPhrase, EncodedMoves, MoveEncoder}
import com.razorfish.icfp_2015.models._
import scalaz.Scalaz.unfold

/**
 * A strategy that keeps a state and calculates a move at a time
 * @tparam State
 */
trait SteppedStrategy[State] extends Strategy {
  def initialState: State

  def moveEncoder: MoveEncoder

  def step(gc: ActiveGameConfiguration, state: State): (GameMove, State)

  def apply(board: Board, source: Source): EncodedMoves = {
    val initialConfiguration = GameConfiguration(board, source)
    val moves = unfold((initialState, initialConfiguration)) {
      case (state, gc@ActiveGameConfiguration(_, _, _, _, _)) =>
        val (move, newState) = step(gc, state)
        val newGC = gc.update(move)
        Option((move, newGC), (newState, newGC))
      case (_, gc@CompletedGameConfiguration(_, _)) =>
        None
    }.map(_._1)
    moveEncoder.encode(moves, phrases)
  }
}

trait SteppedEncodedStrategy[State] extends Strategy {
  def initialState: State

  def phrases: Set[String]

  def step(gc: ActiveGameConfiguration, state: State): (Char, State)

  def apply(board: Board, source: Source): EncodedMoves = {
    val initialConfiguration = GameConfiguration(board, source)
    val moves = unfold((initialState, initialConfiguration)) {
      case (state, gc@ActiveGameConfiguration(_, _, _, _, _)) =>
        val (move, newState) = step(gc, state)
        val newGC = gc.update(MoveEncoder.decodeMove(move))
        Option((move, newGC), (newState, newGC))
      case (_, gc@CompletedGameConfiguration(_, _)) =>
        None
    }.map(_._1)
    EncodedMoves(moves, PowerPhrase.scoreMoves(moves.mkString, phrases))
  }
}