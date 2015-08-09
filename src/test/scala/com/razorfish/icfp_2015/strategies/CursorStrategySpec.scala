package com.razorfish.icfp_2015.strategies

import java.io.File

import com.razorfish.icfp_2015.json.{Output, Parse}
import com.razorfish.icfp_2015.{DumbEncoder, Config, MoveEncoder}

class CursorStrategySpec extends StrategySpec {

   "problem_0 simulation" in {

     skipped

     val file = new File("src/test/resources/problems/problem_1.json")
     val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
     //val expectedScore = Some(3261L)

     val output = Output(0, 0, None, "ei! ei! ei! ei! ei! ei! ei! ei! ei! ei!ei! ei! ei! ei!ei! ei! ei! ei! ei! ei! ei!ei! e")
     specOutput(output, Parse(file), 0, None, true)
   }

   "problem_0 simulation" in {

     skipped

     val file = new File("src/test/resources/problems/problem_0.json")
     val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
     def strategy(phrases: Set[String]) = new CurserStrategy(DumbEncoder, MoveEncoder.phrasesOfPower)
     spec(file, strategy, config, None, true)
   }

 }
