package com.razorfish.icfp_2015.models

sealed trait TileState
case object EmptyTile extends TileState
case object FilledTile extends TileState
