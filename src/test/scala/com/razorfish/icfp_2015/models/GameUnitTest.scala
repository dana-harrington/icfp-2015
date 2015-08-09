package com.razorfish.icfp_2015.models

import org.specs2.mutable._

class GameUnitTest extends Specification {

  "GameUnitTest" should {
    val gu = GameUnit(
      members = Set(
        Cell(1, 1),
        Cell(3, 1)),
      pivot = Cell(2, 1))

    "move W" in {
      val expected = GameUnit(
        members = Set(
          Cell(0, 1),
          Cell(2, 1)
        ),
        pivot = Cell(1, 1)
      )
      gu.move(W) === expected
    }

  }

  "center" should {
    val gu = GameUnit(
      members = Set(
        Cell(1, 1),
        Cell(3, 1)),
      pivot = Cell(2, 1))

    val gu2 = GameUnit(
      members = Set(
        Cell(1, 1),
        Cell(3, 1),
        Cell(0, 2)),
      pivot = Cell(2, 1))

    "place valid unit - even" in {
      val board = Board(10, 10, Set.empty)
      val centered = gu.center(board).get
      centered.members.toSeq must contain(exactly(Cell(3, 0), Cell(5, 0)))
    }

    "place valid unit - odd" in {
      val board = Board(9, 9, Set.empty)
      val centered = gu.center(board).get
      centered.members.toSeq must contain(exactly(Cell(3, 0), Cell(5, 0)))
    }

    "place 2 row valid unit - even" in {
      val board = Board(10, 10, Set.empty)
      val centered = gu2.center(board).get
      centered.members.toSeq must contain(exactly(Cell(4, 0), Cell(6, 0), Cell(3, 1)))
    }

    "place 2 row valid unit - odd" in {
      val board = Board(9, 9, Set.empty)
      val centered = gu2.center(board).get
      centered.members.toSeq must contain(exactly(Cell(3, 0), Cell(5, 0), Cell(2, 1)))
    }

    "not allow unit if no space" in {
      val board = Board(10, 10, Set(Cell(3, 0)))
      gu.center(board) must beNone
    }
  }

  "Testing for cycles" should {
    val gu = GameUnit(
      members = Set(
        Cell(1, 1),
        Cell(3, 1)),
      pivot = Cell(2, 1))

    "pass an empty sequence" in {
      gu.containsCycle(Seq.empty) must beFalse
    }

    "fail a simple cycle" in {
      gu.containsCycle(Seq(W,E)) must beTrue
    }

    "fail a rotation cycle" in {
      gu.containsCycle(Seq(CW, CW, CW, CW, CW, CW, E, W)) must beTrue
    }

  }
}
