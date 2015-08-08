package com.razorfish.icfp_2015

import java.io.File

import com.razorfish.icfp_2015.models.UnitSource
import com.razorfish.icfp_2015.strategies.{PhraseAfterthoughtStrategy, ReallyStupidAI}
import play.api.libs.json._

import com.razorfish.icfp_2015.json.{Parse, Output}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

case class Config( inputFiles: Seq[File] = Seq.empty,
                   powerPhrases: Seq[String] = Seq.empty,
                   memoryLimit: Option[Int] = None,
                   timeLimit: Option[Int] = None)

object Main {

  val argsParser = new scopt.OptionParser[Config]("ifcp_2015") {
    opt[File]('f', "file") required() unbounded() valueName "<file>" action { (x, c) =>
      c.copy(inputFiles = c.inputFiles :+ x) } text "-f is a required file property"

    opt[Int]('t', "time") optional() valueName "<time limit in seconds>" action { (x, c) =>
      c.copy(timeLimit = Some(x)) } text "-t sets a time limit in seconds"

    opt[Int]('m', "memory") optional() valueName "<memory limit in MB>" action { (x, c) =>
      c.copy(memoryLimit = Some(x)) } text "-m sets a memory limit in MB"

    opt[String]('p', "phrase") unbounded() valueName "<power phrase>" action { (x, c) =>
      c.copy(powerPhrases = c.powerPhrases :+ x) } text "-p power phrase"
  }

  def main(args: Array[String]): Unit = {

    argsParser.parse(args, Config()).foreach { config =>
      // TODO: handle memory limit
      // TODO: handle time limit (dump out any problems that are completed near end of limit)

      val gameExecutions = config.inputFiles.map {
        //new GameExecution(_, timelimitSec, memoryLimitMB, phrasesOfPower.toSet)
        new GameExecution(_, config.timeLimit, config.memoryLimit, config.powerPhrases.toSet)
      }

      //println(gameExecutions.map(_.toString).mkString(",\n"))

      val futures = Future.sequence(gameExecutions.map { game =>
        Future {
          game.run
        }
      })


      val results = Await.ready(futures, config.timeLimit.getOrElse(Int.MaxValue).seconds).value.get

      val returnValue = (results match {
        case Success(t) => t.flatten.toSeq
        case Failure(e) => throw e
      }).map(Json.toJson(_)(Output.format))

      println(Json.prettyPrint(Json.toJson(returnValue)))
    }
  }

}

case class GameExecution(file: File,
                         timelimitSec: Option[Int],
                         memoryLimitMB: Option[Int],
                         phrasesOfPower: Set[String]) {

  val parse = Parse(file)

  def run: Seq[Output] = {

    parse.sourceSeeds.map { seed =>
      val source = new UnitSource(seed, parse.gameUnits, parse.sourceLength)
      val strategy = PhraseAfterthoughtStrategy(ReallyStupidAI, DumbEncoder)
      val gameplay = strategy(parse.board, source, phrasesOfPower)
      Output(parse.problemId, seed, None, gameplay.moves.mkString)
    }
  }

}
