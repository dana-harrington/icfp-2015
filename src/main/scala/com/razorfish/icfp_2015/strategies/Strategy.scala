package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.{MoveEncoder, EncodedMoves}
import com.razorfish.icfp_2015.models._

trait Strategy {
  def apply(board: Board, source: Source, phrases: Set[String]): EncodedMoves
}





