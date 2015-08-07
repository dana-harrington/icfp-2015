package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models.GameMove


case class EncodedMoves(moves: Seq[Char], powerWordScore: Score)

trait MoveEncoder {
  def encode(moves: Seq[GameMove]): EncodedMoves
}
