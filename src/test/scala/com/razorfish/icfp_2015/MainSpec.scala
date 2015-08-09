package com.razorfish.icfp_2015

import org.specs2.mutable.Specification

import com.razorfish.icfp_2015.json.Output
import com.razorfish.icfp_2015.strategies.{CurserStrategy, EiStrategy}

class MainSpec extends Specification {

  "output" should {
    "submit constant" in {

      skipped

      val generated = "ei!ei!yuggothnei!ei!ei!nei!ei!nei!44".dropRight(10)
      val output = Output(8, 0, Submit.generateTag("debug curser"), generated)

      Submit.submit(Seq(output)).isSuccess === true
    }

    "submit single" in {

      skipped

      val tag = Submit.generateTag("debug curser")
      def strategy(phrases: Set[PowerPhrase]) = new EiStrategy(phrases, InLineEncoder)

      //val tag = Submit.generateTag("Cursor")
      //def strategy(phrases: Set[PowerPhrase]) = new CurserStrategy(phrases)

      val outputs = Submit.outputOfProblemForStrategy(strategy, tag, 1)

      outputs.forall(_.tag == tag) === true

      Submit.submit(outputs).isSuccess === true
    }

    "submitAll" in {

      skipped

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
