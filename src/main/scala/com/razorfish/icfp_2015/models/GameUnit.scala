package com.razorfish.icfp_2015.models

case class GameUnit(val members: Set[Cell], pivot: Cell) {

  def tileState(p: Cell): TileState =
    if (members.contains(p)) FilledTile
    else EmptyTile

  def positions: Iterable[Cell] = ???

  def move(gameMove: GameMove): GameUnit = ???
}

object NilGameUnit extends GameUnit(Set.empty, NilCell) {

  override def tileState(p: Cell): TileState = EmptyTile

  override val positions: Iterable[Cell] = Nil

  override def move(gameMove: GameMove) = NilGameUnit
}
