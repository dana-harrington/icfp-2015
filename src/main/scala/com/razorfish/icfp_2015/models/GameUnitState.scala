package com.razorfish.icfp_2015.models

trait GameUnitState {
  def unit: GameUnit
  def position: Position
  def orientation: Orientation
}

class GameUnitStateImpl extends GameUnitState {
  override def unit: GameUnit = ???

  override def position: Position = ???

  override def orientation: Orientation = ???
}
