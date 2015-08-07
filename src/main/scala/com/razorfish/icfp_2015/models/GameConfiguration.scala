package com.razorfish.icfp_2015.models

trait GameConfiguration {
  def board: Board
  def activeUnit: Option[GameUnit]
  def source: Iterable[GameUnit]
  def update(move: GameMove): GameConfiguration
}

case class GameConfigurationImpl( board: Board,
                             activeUnit: Option[GameUnit],
                             source: Iterable[GameUnit]) extends GameConfiguration {

  def update(move: GameMove): GameConfiguration = {
    ???
  }

}
