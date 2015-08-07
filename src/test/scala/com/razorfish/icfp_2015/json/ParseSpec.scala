package com.razorfish.icfp_2015.json

import org.specs2.mutable._
import java.io.File

class ParseSpec extends org.specs2.mutable.Specification {

  "problem_0" should {
    val file = new File("src/test/resources/problems/problem_0.json")

    "parse fromFile" in {
      val input = Parse.fromFile(file)
      input.id === 0
      input.sourceSeeds must have size 1
      input.sourceSeeds.head === 0
      input.units must have size 18
    }

  }

  "problem_21" should {

    val file = new File("src/test/resources/problems/problem_21.json")

    "parse fromFile" in {
      val input = Parse.fromFile(file)
      input.id === 21
      input.sourceSeeds must have size 1
      input.sourceSeeds.head === 0
      input.units must have size 1
      input.filled must have size 45
    }

    /*
    "board" in {
      val board = Parse.createBoard(file)
      board.print()
      success
    }
    */
  }

  val problems = (0 to 23).map(i => new File(s"src/test/resources/problems/problem_$i.json"))

  "all problems" in {
    "parse" in {
      problems.forall {
        case file =>
          val board = Parse.createBoard(file)
          println("----------------------------------------------------")
          println(file.getPath)
          println("----------------------------------------------------")
          board.print()
          true
      } must beTrue
    }
  }
}
