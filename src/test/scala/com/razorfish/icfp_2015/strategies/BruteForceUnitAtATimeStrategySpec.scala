package com.razorfish.icfp_2015.strategies

import java.io.File

import com.razorfish.icfp_2015._
import com.razorfish.icfp_2015.json.{Output, Parse}

class BruteForceUnitAtATimeStrategySpec extends StrategySpec {

  "replay constant" in {

    skipped

    val file = new File("src/test/resources/problems/problem_0.json")
    val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
    //val expectedScore = Some(3261L)

    val solution = "ei! ei! ei! ei! ei! ei! ei! ei! ei! ei!ei! ei! ei! ei! ei! ei! ei! ei! ei! ei"
    val output = Output(0, 0, None, solution)
    specOutput(output, Parse(file), 0, None, true, MoveEncoder.phrasesOfPower)
  }

  "simulation" in {

    skipped

    val file = new File("src/test/resources/problems/problem_0.json")
    val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
    def strategy(phrases: Set[PowerPhrase]) = new BruteForceUnitAtATimeStrategy(InLineEncoder, phrases)
    spec(file, strategy, config, MoveEncoder.phrasesOfPower, None, true)
  }

}
