package com.razorfish.icfp_2015.strategies

import java.io.File

import com.razorfish.icfp_2015.json.{Output, Parse}
import com.razorfish.icfp_2015.{Config, Submit}

class EiStrategySpec extends StrategySpec {

  "problem_0 simulation" in {

    skipped

    val file = new File("src/test/resources/problems/problem_0.json")
    val config = Config(Seq(file), Submit.phrasesOfPower, None, None)
    //val expectedScore = Some(3261L)

    val output = Output(0, 0, Submit.generateTag("EiStrategy"), "Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!Ei!EEi!Ei!Ei!Ei!Ei!Ei!Ei!EEi!Ei!Ei!Ei!Ei!Ei!Ei!Ei!EEi!Ei!Ei!EEEi!Ei!EEEEi!Ei!EEEi!Ei!Ei!Ei!Ei!Ei!EEi!Ei!Ei!Ei!Ei!")
    specOutput(output, Parse(file), 0, None, true)
  }

  "problem_0 simulation" in {

    skipped

    val file = new File("src/test/resources/problems/problem_1.json")
    val config = Config(Seq(file),  Submit.phrasesOfPower, None, None)
    spec(file, new EiStrategy, config, None, true)
  }

}
