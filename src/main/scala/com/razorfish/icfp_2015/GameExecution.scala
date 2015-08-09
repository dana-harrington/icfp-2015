package com.razorfish.icfp_2015

import java.io.File

import com.razorfish.icfp_2015.json.{Output, Parse}
import com.razorfish.icfp_2015.models.UnitSource
import com.razorfish.icfp_2015.strategies.Strategy

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GameExecution(strategy: Strategy,
                    parse: Parse,
                    seed: Long,
                    tag: Option[String],
                    timelimitSec: Option[Int],
                    memoryLimitMB: Option[Int],
                    phrasesOfPower: Set[String]) {

  val source = new UnitSource(seed, parse.gameUnits, parse.sourceLength)

  def run: Future[Output] = Future {
    val gameplay = strategy(parse.board, source, phrasesOfPower)
    Output(parse.problemId, seed, tag, gameplay.moves.mkString)
  }
}

object GameExecution {

  def loadFile(strategy: Strategy,
               file: File,
               tag: Option[String],
               timelimitSec: Option[Int],
               memoryLimitMB: Option[Int],
               phrasesOfPower: Set[String]): Seq[GameExecution] = {

    val parse = Parse(file)

    parse.sourceSeeds.map {
      new GameExecution(strategy, parse, _, tag, timelimitSec, memoryLimitMB, phrasesOfPower)
    }
  }
}

