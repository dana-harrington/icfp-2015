package com.razorfish.icfp_2015

import org.specs2.mutable.Specification

import com.razorfish.icfp_2015.json.Output
import com.razorfish.icfp_2015.strategies.{CurserStrategy, EiStrategy}

class MainSpec extends Specification {

  "output" should {
    "submit constant" in {

      skipped

      val generated = "ei! ei! ei! ei! ei! ei! ei! ei! ei! ei!ei! ei! ei! ei!ei! ei! ei! ei! ei! ei! ei!ei! ei"
      val fail = generated.take(30) + "eeeeeeee"
      val output = Output(8, 0, Submit.generateTag("eistrategy"), "ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ")
      val cursorOutput = Output(0, 0, Submit.generateTag("cursor -10"), "ei!ei!ei!ei!ei!ei!ei!ei!nnei!ei!ei!ei!ei!ei!ei!ei!nyuggothyuggothei!nyuggothyuggoth4yuggothei!ei!4yuggothei!nnyuggothei!ei!44yuggothei!4ei!4444nei!ei!4nei!nei!4".dropRight(10))

      val s = (0 to 1).map {
        case i =>
          val g = generated.dropRight(i)
          Output(1, 0, Submit.generateTag("generated -- " + i), g)
      }
      Submit.submit(Seq(cursorOutput)).isSuccess === true
    }

    "submit single" in {

      skipped

      val tag = Submit.generateTag("EiStrategy")
      def strategy(phrases: Set[PowerPhrase]) = new EiStrategy(phrases)

      //val tag = Submit.generateTag("Cursor")
      //def strategy(phrases: Set[PowerPhrase]) = new CurserStrategy(phrases)

      val outputs = Submit.outputOfProblemForStrategy(strategy, tag, 1)

      outputs.forall(_.tag == tag) === true

      Submit.submit(outputs).isSuccess === true
    }

    "submitAll" in {

      //skipped

      //val tag = Submit.generateTag("EiStrategy")
      //def strategy(phrases: Set[String]) = new EiStrategy(phrases)

      val tag = Submit.generateTag("Curser")
      def strategy(phrases: Set[PowerPhrase]) = new CurserStrategy(phrases)

      val outputs = Submit.outputsOfAllProblemsForStrategy(strategy, tag)

      outputs.forall(_.tag == tag) === true

      Submit.submit(outputs).isSuccess === true
    }
  }
}
