package com.razorfish.icfp_2015

import org.specs2.mutable.Specification
import scala.math.pow

class RNGProviderSpec extends Specification {
  "RNGP" should {
    "return the sequence for seed 17" in {
      val m = pow(2, 32).toLong
      val a = 1103515245
      val c = 12345
      val seed = 17

      val rngp = new RNGProvider(m, a, c, seed)

      //TODO: Figure out the 0 case
      //rngp.next must beEqualTo(0)
      rngp.next must beEqualTo(24107)
      rngp.next must beEqualTo(16552)
      rngp.next must beEqualTo(12125)
      rngp.next must beEqualTo(9427)
      rngp.next must beEqualTo(13152)
      rngp.next must beEqualTo(21440)
      rngp.next must beEqualTo(3383)
      rngp.next must beEqualTo(6873)
      rngp.next must beEqualTo(16117)

    }
  }
}