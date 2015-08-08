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

  def center(board: Board): Option[GameUnit] = {
    val activeUnitColumns = members.seq.map(_.column)
    val left = activeUnitColumns.min
    val right = activeUnitColumns.max
    val top = members.seq.map(_.row).min

    val movesToRight = ((board.width - 1 - (right - left)) / 2) - left
    val movesToTop = top

    val newMembers = members.map {
      case Cell(x, y) => Cell(x + movesToRight, y - top)
    }
    val newPivot = Cell(pivot.column + movesToRight, pivot.row - top)

    val horizontallyCenteredGameUnit = GameUnit(newMembers, newPivot)

    if (horizontallyCenteredGameUnit.members.map(board.tileState).forall(_ == EmptyTile)) Some(horizontallyCenteredGameUnit)
    else None
  }
}

object NilGameUnit extends GameUnit(Set.empty, NilCell) {

  override def tileState(p: Cell): TileState = EmptyTile

  override def move(gameMove: GameMove) = NilGameUnit
}
