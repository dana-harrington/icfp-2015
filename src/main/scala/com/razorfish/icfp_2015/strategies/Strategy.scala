package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.EncodedMoves
import com.razorfish.icfp_2015.models._

trait Strategy {
  def phrases: Set[String]
  def apply(board: Board, source: Source): EncodedMoves
}





