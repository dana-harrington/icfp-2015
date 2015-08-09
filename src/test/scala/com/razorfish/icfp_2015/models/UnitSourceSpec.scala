package com.razorfish.icfp_2015.models

import org.specs2.mutable.Specification

import com.razorfish.icfp_2015.json.{ParseSpec, Parse, Output}
import com.razorfish.icfp_2015.strategies.{CurserStrategy, EiStrategy}

class UnitSourceSpec extends Specification {

  "unit source should end" in {
    val parse = Parse(ParseSpec.problems(1))
    val unitSource = new UnitSource(parse.sourceSeeds.head, parse.gameUnits, parse.sourceLength)
    parse.sourceLength === 100
    val allUnits = unitSource.toSeq

    allUnits must have size parse.sourceLength
  }
}
