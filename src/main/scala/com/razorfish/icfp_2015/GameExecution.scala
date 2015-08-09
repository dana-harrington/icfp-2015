package com.razorfish.icfp_2015

import java.io.File

import com.razorfish.icfp_2015.json.{Output, Parse}
import com.razorfish.icfp_2015.models.UnitSource
import com.razorfish.icfp_2015.strategies.StrategyBuilder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GameExecution(strategy: StrategyBuilder,
                    parse: Parse,
                    seed: Long,
                    tag: Option[String],
                    timelimitSec: Option[Int],
                    memoryLimitMB: Option[Int],
                    phrasesOfPower: Set[PowerPhrase]) {

  val source = new UnitSource(seed, parse.gameUnits, parse.sourceLength).toSeq

  def run: Future[Output] = Future {
    val gameplay = strategy(phrasesOfPower)(parse.board, source)
    Output(parse.problemId, seed, tag, gameplay.moves.mkString)
  }
}

object GameExecution {

  def loadFile(strategy: StrategyBuilder,
               file: File,
               tag: Option[String],
               timelimitSec: Option[Int],
               memoryLimitMB: Option[Int],
               phrasesOfPower: Set[PowerPhrase]): Seq[GameExecution] = {

    val parse = Parse(file)

    parse.sourceSeeds.map {
      new GameExecution(strategy, parse, _, tag, timelimitSec, memoryLimitMB, phrasesOfPower)
    }
  }
}

