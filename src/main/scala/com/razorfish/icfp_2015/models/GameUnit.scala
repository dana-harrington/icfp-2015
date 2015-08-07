package com.razorfish.icfp_2015.models

case class GameUnit(members: Set[Cell], pivot: Cell) {

  def move(move: GameMove): GameUnit = {
    val newMembers = members.map(_.move(move, pivot))
    val newPivot = pivot.move(move, pivot)
    GameUnit(newMembers, newPivot)
  }

}


