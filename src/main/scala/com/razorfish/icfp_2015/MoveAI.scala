package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models.{SW, GameMove, GameConfigurationImpl}

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
