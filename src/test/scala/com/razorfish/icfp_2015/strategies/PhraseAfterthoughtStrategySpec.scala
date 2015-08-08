package com.razorfish.icfp_2015.strategies

import java.io.File

import com.razorfish.icfp_2015.{Config, DumbEncoder}

class PhraseAfterthoughtStrategySpec extends StrategySpec {
  val strategy = PhraseAfterthoughtStrategy(ReallyStupidAI, DumbEncoder)


  "problem_21 simulation" in {
    skipped
    val file = new File("src/test/resources/problems/problem_21.json")
    val config = Config(Seq(file), Set("Ei!"), None, None)
    spec(file, strategy, config)
  }

}


