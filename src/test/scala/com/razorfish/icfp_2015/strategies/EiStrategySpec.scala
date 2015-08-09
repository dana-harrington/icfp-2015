package com.razorfish.icfp_2015.strategies

import java.io.File

import com.razorfish.icfp_2015.json.{Output, Parse}
import com.razorfish.icfp_2015._

class EiStrategySpec extends StrategySpec {

  "problem_0 simulation" in {

    skipped

    val file = new File("src/test/resources/problems/problem_0.json")
    val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
    //val expectedScore = Some(3261L)

    val output = Output(0, 0, Submit.generateTag("EiStrategy"), "ei!ei!ei!ei!ei!ei!ei!ei!ei!ei!ei!ei!ei!ei!ei!ei!ei!eei!ei!ei!ei!ei!ei!ei!eei!ei!ei!ei!ei!ei!ei!ei!eei!ei!ei!eeei!ei!eeeei!ei!eeei!ei!ei!ei!ei!ei!eei!ei!ei!ei!ei!")
    specOutput(output, Parse(file), 0, None, true)
  }

  "problem_0 simulation" in {

    skipped

    val file = new File("src/test/resources/problems/problem_1.json")
    val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
    def eiStrategy(phrases: Set[PowerPhrase]) = new EiStrategy(phrases)
    spec(file, eiStrategy, config, None, true)
  }

}
