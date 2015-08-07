package com.razorfish.icfp_2015.models

trait Board {
  def tileState(p: Position): TileState
  def positions: Iterable[Position]
}
