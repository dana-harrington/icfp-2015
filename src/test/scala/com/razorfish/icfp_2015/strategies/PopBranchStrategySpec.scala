package com.razorfish.icfp_2015.strategies

import java.io.File

import com.razorfish.icfp_2015._
import com.razorfish.icfp_2015.json.{Output, Parse}

class PopBranchStrategySpec extends StrategySpec {

  /*
  Problem 15

  eistrategy
  "ei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!ei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!ei!lei!lei!lei!lei!lei!lei!ei!lei!"
  560

  pop ei strategy
  "ei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!l"
  552

  pop
  "ia! ia!lia! ia!lia! ia!lia! ia!lia! ia!l"
  390

  pop
  "yuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothb"
  506

  pop
  ei!r'lyehr'lyehr'lyehr'lyehr'lyehr'lyehdplbbbapr'lyehr'lyehr'lyehr'lyehr'lyehei!r'lyehr'lyehr'lyehr'lyehpr'lyehr'lyehdplbapppppppei!ppppppppppppppp
  858
   */
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

    //skipped

    val file = new File("src/test/resources/problems/problem_15.json")
    val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
    def strategy(phrases: Set[PowerPhrase]) = new POPBranchStrategy(MoveEncoder.phrasesOfPower, InLineEncoder)
    spec(file, strategy, config, MoveEncoder.phrasesOfPower, None, true)
  }

}
