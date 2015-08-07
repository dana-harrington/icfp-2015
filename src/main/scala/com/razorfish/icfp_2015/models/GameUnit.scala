package com.razorfish.icfp_2015.models

trait GameUnit {
  def tileState(p: Position): TileState

  def positions: Iterable[Position]

  def move(gameMove: GameMove): GameUnit
}

class GameUnitImpl(val positions: Seq[Position], pivot: Position) extends GameUnit {
  def tileState(p: Position): TileState =
    if (positions.contains(p)) FilledTile
    else EmptyTile

  override def move(gameUnit: GameMove): GameUnit = ???
}

object NilGameUnit extends GameUnit {
  def tileState(p: Position): TileState = EmptyTile

  val positions = Nil


  override def move(gameMove: GameMove) = NilGameUnit
}