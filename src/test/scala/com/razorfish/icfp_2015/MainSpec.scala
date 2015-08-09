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
      val output = Output(1, 0, Submit.generateTag("generated - 1"), fail)


      val s = (0 to 1).map {
        case i =>
          val g = generated.dropRight(i)
          Output(1, 0, Submit.generateTag("generated -- " + i), g)
      }
      Submit.submit(s).isSuccess === true
    }

    "submitAll" in {

      skipped

      //val tag = Submit.generateTag("EiStrategy")
      //def strategy(phrases: Set[String]) = new EiStrategy(phrases)

      val tag = Submit.generateTag("Cursor")
      def strategy(phrases: Set[String]) = new CurserStrategy(DumbEncoder, phrases)

      val outputs = Submit.outputsOfAllProblemsForStrategy(strategy, tag)

      outputs.forall(_.tag == tag) === true

      Submit.submit(outputs).isSuccess === true
    }
  }
}
