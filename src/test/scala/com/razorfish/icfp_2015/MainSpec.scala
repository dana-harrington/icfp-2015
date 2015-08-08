package com.razorfish.icfp_2015

import org.specs2.mutable.Specification

import com.razorfish.icfp_2015.json.Output

class MainSpec extends Specification {

  "output" should {
    "submit" in {
      skipped
      val output = Output(21, 0, None, "44444444444444444444")

      Submit.submit(Seq(output)).isSuccess === true
    }
  }
}
