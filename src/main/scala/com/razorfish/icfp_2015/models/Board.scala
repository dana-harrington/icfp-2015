package com.razorfish.icfp_2015.models

trait Board {
  def tileState(p: Cell): CellState
  def positions: Iterable[Cell]
  def width: Int
  def height: Int
}
