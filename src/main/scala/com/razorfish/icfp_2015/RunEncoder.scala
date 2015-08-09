package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models._

object RunEncoder {
  def main(args: Array[String]): Unit = {
    //println(s"Nothing so far with args: ${args:_*}" )

    val powerWords = args.toSet

    val moves = Seq(
      E,SW,W,                                 //"Ei!" -> (E,SW,W)
      SW,E,CW,SW,E,SW,CW,SW,E,CW,SW,          //"abracadabra" -> (SW,E,CW,SW,E,SW,CW,SW,E,CW,SW)
      W,W,W,W,W,
      E,SW,W,                                 //"Ei!"
      W,W,
      SW,E,CW,SW,E,SW,CW,SW,E,CW,SW,          //"abracadabra"
      W,W,
      SW,SE,SW,SW,SE,SE,E                     //"Johanne" -> (SW,SE,SW,SW,SE,SE,E)
    )


    val encoded = InLineEncoder.encode(moves, powerWords)
    println(s"\n$encoded\n")
  }

}
