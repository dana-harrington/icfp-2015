package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.models.{GameConfigurationImpl, GameMove, SW}

trait MoveAI {
  def step(gc: GameConfigurationImpl): GameMove
}

object ReallyStupidAI extends MoveAI {
  def step(gc: GameConfigurationImpl): GameMove = {
    SW
  }
}

/**
 * Always picks a non-freeze move
 */
object QuiteStupidAI extends MoveAI {
  override def step(gc: GameConfigurationImpl): GameMove = {
    val moves = GameMove.moves
    val (nonFreezeMoves, freezeMoves) = moves.partition(gc.tryMove(_).isDefined)
    nonFreezeMoves.headOption.orElse(freezeMoves.headOption).head
  }
}
