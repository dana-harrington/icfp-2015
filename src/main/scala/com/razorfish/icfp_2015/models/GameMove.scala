package com.razorfish.icfp_2015.models

sealed trait GameMove
case object W extends GameMove
case object SW extends GameMove
case object SE extends GameMove
case object E extends GameMove
case object CW extends GameMove
case object CCW extends GameMove