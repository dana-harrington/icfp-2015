package com.razorfish.icfp_2015

import java.io.File

import play.api.libs.json.Json

import com.razorfish.icfp_2015.json.{Parse, Output}
import com.razorfish.icfp_2015.models._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]): Unit = {

    /*
    val initialBoard = parseBoard
    val source = parseSource
    val initialConfiguration = GameConfiguration(initialBoard, source)
    val ai: AI = ???
    val moveEncoder: MoveEncoder = ???
    val moves: Stream[(GameMove, GameConfiguration)] = unfold(initialConfiguration){
      case gc@GameConfigurationImpl(_, _, _, _, _) =>
        val move = ai.step(gc)
        val newGC = gc.update(move)
        Some((move, newGC), newGC)
      case gc@GameDoneConfiguration(_,_) =>
        None
    }
    println("Nothing so far")

    val board = new Board(10, 15, Set.empty)
    val testGameUnit = new GameUnit(Set(Cell(1,2), Cell(2,2), Cell(3,2), Cell(2,3)), Cell(1,2))
    board.print(testGameUnit)

*/
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

    parse.sourceSeeds.map {
      seed => Output(parse.problemId, seed, "TODO TAG HERE", "Ei!Ei!Ei!Ei!Ei!Ei!")
    }
  }
}
