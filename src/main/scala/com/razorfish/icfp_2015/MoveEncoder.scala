package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models.{W, SW, SE, E, CW, CCW, GameMove}


case class EncodedMoves(moves: Seq[Char], powerWordScore: Score)

trait MoveEncoder {
  def encode(moves: Seq[GameMove]): EncodedMoves
}

object DumbEncoder extends MoveEncoder {
  override def encode(moves: Seq[GameMove]): EncodedMoves = {
    val encodedMoves = moves.map{ move =>
      MoveEncoder.moveEncodings(move).head
    }
    EncodedMoves(encodedMoves, 0)
  }
}

object MoveEncoder {
  val moveEncodings: Map[GameMove, Set[Char]] = Map(
    W ->
      Set('p', '\'', '!', '.', '0', '3'),
    SW ->
      Set('a','g','h','i','j','4'),
    SE ->
      Set('l','m','n','o',' ', '5'),
    E ->
      Set('b','c','e','f','y'),
    CW ->
      Set('d','q','r','v','z','l'),
    CCW ->
      Set('k','s','t','u','w','x')
  )
}