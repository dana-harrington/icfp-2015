package com.razorfish.icfp_2015.models

import org.specs2.mutable._

class CellTest extends org.specs2.mutable.Specification {

  "A Cell" should {
    val c = Cell(1, 3)

    "translate W correctly" in {
      c.translate(W) === Cell(0, 3)
    }


    "rotate around a pivot point" in {
      val p = Cell(2, 5)
      val cubeVector = c.cubeCoords - p.cubeCoords
      cubeVector === CubeCoord(0,2,-2)
      c.rotate(CW, Cell(2, 5)) === Cell(3, 3)
    }

    "be unchanged by conversions to cube coordinates and back" in {
      c.cubeCoords.cell == c
    }
  }

  "A CubeCoordinate" should {
    val cc = CubeCoord(3, 0, -3)
    "rotate about the 0 point" in {
      cc.rotate(CW) === CubeCoord(3, -3, 0)
    }
  }


}
