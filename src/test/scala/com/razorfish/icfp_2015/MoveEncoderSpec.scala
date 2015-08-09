package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models._
import org.specs2.mutable.Specification

class MoveEncoderSpec  extends Specification {

  "encode moves" in {

    val moves = Seq(
      E, SW, W, //"Ei!" -> (E,SW,W)
      SW, E, CW, SW, E, SW, CW, SW, E, CW, SW,  //"abracadabra" -> (SW,E,CW,SW,E,SW,CW,SW,E,CW,SW)
      W, SE,
      W, W,
      SW, E, CW, SW, E, SW, CW, SW, E, CW, SW,  //"abracadabra"
      W, W,
      SW, SE, SW, SW, SE, SE, E,                //"Johanne" -> (SW,SE,SW,SW,SE,SE,E)
      E, SW, W, SW, E, SW, CW, SW, E, CW, SW    //"ei!acadabra"
    )

    val powerWords = Set("ei!", "Johanne", "abracadabra", "ei!acadabra")

    val encoded = InLineEncoder.encode(moves, powerWords)
    encoded.moves === "ei!abracadabraplppabracadabrappJohanneei!acadabra".toSeq
  }
}
