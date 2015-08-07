package com.razorfish.icfp_2015.models

trait GameConfiguration {
  def board: Board
  def activeUnit: Option[GameUnit]
  def source: Iterable[GameUnit]
  def update(move: GameMove): GameConfiguration
}

class GameConfigurationImpl extends GameConfiguration {
  override def board: Board = ???

  override def activeUnit: Option[GameUnit] = ???

  override def update(move: GameMove): GameConfiguration = ???

  override def source: Iterable[GameUnit] = ???
}
