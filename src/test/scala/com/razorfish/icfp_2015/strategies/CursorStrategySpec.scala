package com.razorfish.icfp_2015.strategies

import java.io.File

import com.razorfish.icfp_2015.json.{Output, Parse}
import com.razorfish.icfp_2015.models.{Cell, GameUnit, GameConfiguration, Board}
import com.razorfish.icfp_2015.{DumbEncoder, Config, MoveEncoder}

class CurserStrategySpec extends StrategySpec {

   "problem_0 simulation" in {

     skipped

     val file = new File("src/test/resources/problems/problem_1.json")
     val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
     //val expectedScore = Some(3261L)

     val output = Output(0, 0, None, "ei! ei! ei! ei! ei! ei! ei! ei! ei! ei!ei! ei! ei! ei!ei! ei! ei! ei! ei! ei! ei!ei! e")
     specOutput(output, Parse(file), 0, None, true, MoveEncoder.phrasesOfPower)
   }

   "problem_0 simulation" in {

     skipped

     val file = new File("src/test/resources/problems/problem_0.json")
     val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
     def strategy(phrases: Set[String]) = new CurserStrategy(MoveEncoder.phrasesOfPower)
     spec(file, strategy, config, MoveEncoder.phrasesOfPower, None, true)
   }

  "The curser" should {
    "not be tricked into using power phrases with cycles" in {
      val badPhrase = "f'tn"
      val board = Board(10, 10, Set.empty)
      val source = Seq(GameUnit(Set(Cell(1,1), Cell(2,1)), Cell(1,1))).iterator

      def strategy(phrases: Set[String]) = new CurserStrategy(MoveEncoder.phrasesOfPower)
      val play = strategy(Set(badPhrase)).apply(board, source)
      play.moves.mkString.contains(badPhrase) should beFalse

    }
  }

 }
