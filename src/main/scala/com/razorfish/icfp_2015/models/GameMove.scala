package com.razorfish.icfp_2015.models

sealed trait GameMove
sealed trait Translation extends GameMove
sealed trait Rotation extends GameMove

case object W extends Translation
case object SW extends Translation
case object SE extends Translation
case object E extends Translation
case object CW extends Rotation
case object CCW extends Rotation

object GameMove {
  val moves = Set[GameMove](W,SW,SE,E,CW,CCW)
}
