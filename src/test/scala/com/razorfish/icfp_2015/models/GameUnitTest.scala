package com.razorfish.icfp_2015.models

import org.specs2.mutable._

class GameUnitTest extends Specification {

  "GameUnitTest" should {
    val gu = GameUnit(
      members = Set(
        Cell(1,1),
        Cell(3,1)),
      pivot = Cell(2,1))

    "move W" in {
      val expected = GameUnit(
        members = Set(
          Cell(0,1),
          Cell(2,1)
        ),
        pivot = Cell(1,1)
      )
      gu.move(W) === expected
    }

  }
}
