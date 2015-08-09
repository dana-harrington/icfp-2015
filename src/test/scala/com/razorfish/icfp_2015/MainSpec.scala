package com.razorfish.icfp_2015

import org.specs2.mutable.Specification

import com.razorfish.icfp_2015.json.Output
import com.razorfish.icfp_2015.strategies.EiStrategy

class MainSpec extends Specification {

  "output" should {
    "submit constant" in {

      skipped

      val output = Output(0, 0, Submit.generateTag("EiStrategy"), "ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei")

      Submit.submit(Seq(output)).isSuccess === true
    }
/*
    "submit 0" in {

      val tag = Submit.generateTag("EiStrategy")
      val strategy = new EiStrategy

      val outputs = Submit.outputOfProblemForStrategy(strategy, tag, 1)
      success
    }
*/
    "submitAll" in {

      skipped

      val tag = Submit.generateTag("EiStrategy")
      def strategy(phrases: Set[String]) = new EiStrategy(phrases)
      val outputs = Submit.outputsOfAllProblemsForStrategy(strategy, tag)

      outputs.forall(_.tag == tag) === true

      Submit.submit(outputs).isSuccess === true
    }
  }
}
