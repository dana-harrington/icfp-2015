package com.razorfish.icfp_2015.models

trait GameUnitState {
  def unit: GameUnit
  def members: Set[Cell]
}

// stupidly applied all the moves every time
case class GameUnitStateImpl(unit: GameUnit, moves: Seq[GameMove]) extends GameUnitState {

  val members: Set[Cell] =
    unit.members.map(cell => moves.foldRight(cell)((m, c) => c.move(m, unit.pivot)))

}
