package com.razorfish.icfp_2015.models

sealed trait GameMove
sealed trait Translation
sealed trait Rotation

case object W extends GameMove with Translation
case object SW extends GameMove with Translation
case object SE extends GameMove with Translation
case object E extends GameMove with Translation
case object CW extends GameMove with Rotation
case object CCW extends GameMove with Rotation