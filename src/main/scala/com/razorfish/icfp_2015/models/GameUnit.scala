package com.razorfish.icfp_2015.models

case class GameUnit(members: Set[Cell], pivot: Cell) {

  def tileState(p: Cell): TileState =
    if (members.contains(p)) FilledTile
    else EmptyTile

  def move(move: GameMove): GameUnit = {
    val newMembers = members.map(_.move(move, pivot))
    val newPivot = pivot.move(move, pivot)
    GameUnit(newMembers, newPivot)
  }
}

object NilGameUnit extends GameUnit(Set.empty, NilCell) {

  override def tileState(p: Cell): TileState = EmptyTile

  override def move(gameMove: GameMove) = NilGameUnit
}
