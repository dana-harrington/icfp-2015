package com.razorfish.icfp_2015

import org.specs2.mutable.Specification
import scala.math.pow

class RNGProviderSpec extends Specification {
  "RNGP" should {
    "return the sequence" in {
      val m = pow(2, 32).toLong
      val a = 1103515245
      val c = 12345
      val seed = 17

      val rngp = new RNGProvider(m, a, c)

      1 must beEqualTo(1)
    }
  }
}