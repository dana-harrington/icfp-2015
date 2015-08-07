package com.razorfish.icfp_2015.models

// cells may be off the board
sealed trait CellState

// tiles are on the board, and may be empty or full
sealed trait TileState

case object InvalidTile extends TileState
case object EmptyTile extends CellState with TileState
case object FilledTile extends CellState with TileState
