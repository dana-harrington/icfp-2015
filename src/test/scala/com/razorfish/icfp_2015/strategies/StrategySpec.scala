package com.razorfish.icfp_2015.strategies

import java.io.File

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

import com.razorfish.icfp_2015.{Config, GameExecution, MoveEncoder}
import com.razorfish.icfp_2015.json.Parse
import com.razorfish.icfp_2015.models._
import scala.concurrent.duration._

import scala.concurrent.Await

trait StrategySpec extends Specification {

  def getMove(char: Char): Option[GameMove] = {
    val c = char.toLower
    MoveEncoder.moveEncodings.mapValues(_.contains(c)).filter {
      case (k, v) => v
    }.headOption.map(_._1)
  }

  def solutionToGameMoves(solution: String): Seq[GameMove] = {
    solution.toCharArray.flatMap(getMove(_)).toSeq
  }

  def spec(file: File, strategy: Strategy, config: Config): MatchResult[Boolean] = {
    val parse = Parse(file)
    val seed = parse.sourceSeeds.head
    val gameExecution = new GameExecution(strategy, parse, seed, config.timeLimit, config.memoryLimit, config.powerPhrases.toSet)
    val output = Await.result(gameExecution.run, 20 seconds)
    val gameMoves = solutionToGameMoves(output.solution)
    val source = new UnitSource(seed, parse.gameUnits, parse.sourceLength)

    var gameConfiguration = GameConfiguration(parse.board, source).asInstanceOf[GameConfigurationImpl]
    var gameDone = false

    for (move <- gameMoves) {

      println(move)
      gameConfiguration.board.print(gameConfiguration.activeUnit)

      gameConfiguration.update(move) match {
        case _: GameDoneConfiguration => gameDone = true
        case gc: GameConfigurationImpl => gameConfiguration = gc
      }
    }

    gameDone === true
  }
}
