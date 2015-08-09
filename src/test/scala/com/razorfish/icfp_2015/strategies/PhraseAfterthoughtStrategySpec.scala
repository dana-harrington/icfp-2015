package com.razorfish.icfp_2015.strategies

import java.io.File

import com.razorfish.icfp_2015.{Submit, Config, DumbEncoder}

class PhraseAfterthoughtStrategySpec extends StrategySpec {
  val strategy = PhraseAfterthoughtStrategy(ReallyStupidAI, DumbEncoder)

  "problem_18 simulation" in {
    skipped
    val file = new File("src/test/resources/problems/problem_18.json")
    val config = Config(Seq(file), Submit.phrasesOfPower, None, None)
    spec(file, strategy, config)
  }

  "problem_19 simulation" in {
    skipped
    val file = new File("src/test/resources/problems/problem_19.json")
    val config = Config(Seq(file), Submit.phrasesOfPower, None, None)
    spec(file, strategy, config)
  }

  "problem_21 simulation" in {
    skipped
    val file = new File("src/test/resources/problems/problem_21.json")
    val config = Config(Seq(file), Submit.phrasesOfPower, None, None)
    spec(file, strategy, config)
  }

}


