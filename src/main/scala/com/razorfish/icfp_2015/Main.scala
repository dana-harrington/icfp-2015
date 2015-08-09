package com.razorfish.icfp_2015

import java.io.File

import com.razorfish.icfp_2015.strategies.{PhraseAfterthoughtStrategy, ReallyStupidAI}
import play.api.libs.json._

import com.razorfish.icfp_2015.json.Output
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

case class Config(inputFiles: Seq[File] = Seq.empty,
                  powerPhrases: Set[String] = Set.empty,
                  memoryLimit: Option[Int] = None,
                  timeLimit: Option[Int] = None,
                  cores: Option[Int] = None)

object Main {

  val argsParser = new scopt.OptionParser[Config]("ifcp_2015") {
    opt[File]('f', "file") required() unbounded() valueName "<file>" action { (x, c) =>
      c.copy(inputFiles = c.inputFiles :+ x) } text "-f is a required file property"

    opt[Int]('t', "time") optional() valueName "<time limit in seconds>" action { (x, c) =>
      c.copy(timeLimit = Some(x)) } text "-t sets a time limit in seconds"

    opt[Int]('m', "memory") optional() valueName "<memory limit in MB>" action { (x, c) =>
      c.copy(memoryLimit = Some(x)) } text "-m sets a memory limit in MB"

    opt[String]('p', "phrase") unbounded() valueName "<power phrase>" action { (x, c) =>
      c.copy(powerPhrases = c.powerPhrases + x) } text "-p power phrase"

    //TODO: can we use the available cores?
  }

  def main(args: Array[String]): Unit = {

    argsParser.parse(args, Config()).foreach { config =>
      // TODO: handle memory limit
      // TODO: handle time limit (dump out any problems that are completed near end of limit)

      val gameExecutions = config.inputFiles.flatMap {
        val strategy = PhraseAfterthoughtStrategy(ReallyStupidAI, DumbEncoder)
        GameExecution.loadFile(strategy, _, None, config.timeLimit, config.memoryLimit, config.powerPhrases.toSet)
      }

      val futures = Future.sequence(gameExecutions.map(_.run))

      val results = Await.ready(futures, config.timeLimit.getOrElse(Int.MaxValue).seconds).value.get

      val returnValue = (results match {
        case Success(t) => t
        case Failure(e) => throw e
      }).map(Json.toJson(_)(Output.writes))

      println(Json.prettyPrint(Json.toJson(returnValue)))
    }
  }

}
