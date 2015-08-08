package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.models.{ActiveGameConfiguration, GameMove, SW}

trait MoveAI {
  def step(gc: ActiveGameConfiguration): GameMove
}

object ReallyStupidAI extends MoveAI {
  def step(gc: ActiveGameConfiguration): GameMove = {
    SW
  }
}

/**
 * Always picks a non-freeze move
 */
object QuiteStupidAI extends MoveAI {
  override def step(gc: ActiveGameConfiguration): GameMove = {
    val moves = GameMove.moves
    val (nonFreezeMoves, freezeMoves) = moves.partition(gc.tryMove(_).isDefined)
    nonFreezeMoves.headOption.orElse(freezeMoves.headOption).head
  }
}
