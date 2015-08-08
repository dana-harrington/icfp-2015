package com.razorfish.icfp_2015

import java.io.File

import com.razorfish.icfp_2015.models.UnitSource
import play.api.libs.json.Json

import com.razorfish.icfp_2015.json.{Parse, Output}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]): Unit = {

    val a = new mutable.Stack[String]
    a.pushAll(args.reverse)

    var timelimitSec = Int.MaxValue
    var memoryLimitMB = Int.MaxValue
    val phrasesOfPower = new scala.collection.mutable.HashSet[String]
    val files = new ListBuffer[File]

    while (!a.isEmpty) {
      val h = a.pop()

      if (h.startsWith("-f")) {
        files += new File(
          "src/test/resources/problems/" + (
            if (h == "-f") a.pop()
            else h.drop(2)
            )
        )
      } else if (h.startsWith("-t")) {
        timelimitSec = {
          if (h == "-t") a.pop()
          else h.drop(2)
        }.toInt
      } else if (h.startsWith("-m")) {
        memoryLimitMB = {
          if (h == "-m") a.pop()
          else h.drop(2)
        }.toInt
      } else if (h.startsWith("-p")) {
        phrasesOfPower += {
          if (h == "-p") a.pop()
          else h.drop(2)
        }
      } else throw new Exception("Unknown parameter " + h)
    }

    if (files.isEmpty) throw new Exception("No files specified")

    val gameExecutions = files.map {
      new GameExecution(_, timelimitSec, memoryLimitMB, phrasesOfPower.toSet)
    }

    //println(gameExecutions.map(_.toString).mkString(",\n"))

    val futures = Future.sequence(gameExecutions.map { game =>
      Future {
        game.run
      }
    })

    val results = Await.ready(futures, timelimitSec seconds).value.get

    val returnValue = results match {
      case Success(t) => Json.arr(t.flatten.seq)
      case Failure(e) => throw e
    }

    println(Json.asciiStringify(returnValue))
  }
}

case class GameExecution(file: File,
                         timelimitSec: Int,
                         memoryLimitMB: Int,
                         phrasesOfPower: Set[String]) {

  val parse = Parse(file)

  def run: Seq[Output] = {
    //TODO: run simulation
    println("Running game")

    parse.sourceSeeds.map { seed =>
      val source = new UnitSource(seed, parse.gameUnits, parse.sourceLength)
      val strategy = PhraseAfterthoughtStrategy(ReallyStupidAI, DumbEncoder)
      val gameplay = strategy(parse.board, source, phrasesOfPower)
      Output(parse.problemId, seed, "TODO TAG HERE", gameplay.moves.toString)
    }
  }

}
