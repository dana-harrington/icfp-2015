package com.razorfish.icfp_2015.models

trait GameConfiguration {
  def board: Board
  def activeUnit: Option[GameUnit]
  def source: Iterable[GameUnit]
  def update(move: GameMove): GameConfiguration
}

class GameConfigurationImpl( board: Board,
                             activeUnit: GameUnit,
                             source: Iterable[GameUnit]) extends GameConfiguration {

  def update(move: GameMove): GameConfiguration = {
    ???
  }

}
