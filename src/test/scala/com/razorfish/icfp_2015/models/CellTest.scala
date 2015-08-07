package com.razorfish.icfp_2015.models

import org.specs2.mutable._

class CellTest extends org.specs2.mutable.Specification {

  "A Cell" should {
    val c = Cell(1, 3)

    "translate W correctly" in {
      c.translate(W) === Cell(0, 3)
    }

    "rotate around a pivot point" in {
      c.rotate(CW, Cell(2, 5)) === Cell(3, 3)
    }
  }



}
