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

  def spec(file: File, strategy: Strategy, config: Config): MatchResult[_] = {
    val parse = Parse(file)
    val seed = parse.sourceSeeds.head
    val gameExecution = new GameExecution(strategy, parse, seed, config.timeLimit, config.memoryLimit, config.powerPhrases)
    val output = Await.result(gameExecution.run, 20 seconds)
    val gameMoves = solutionToGameMoves(output.solution)
    val source = new UnitSource(seed, parse.gameUnits, parse.sourceLength)

    var gameConfiguration = GameConfiguration(parse.board, source).asInstanceOf[ActiveGameConfiguration]
    var gameDone = false

    println(s"Game with ${gameMoves.size} moves")
    println(output.solution)
    println("Starting position:")
    gameConfiguration.board.print(gameConfiguration.activeUnit)

    var moveCount = 0
    for (move <- gameMoves) {

      gameConfiguration.update(move) match {
        case _: CompletedGameConfiguration =>
          moveCount += 1
          println(s"Move #$moveCount: ${move}: Game over")
          gameDone = true
        case gc: ActiveGameConfiguration if !gameDone => gameConfiguration = gc
          moveCount += 1
          println(s"Move #$moveCount: ${move}")
          gameConfiguration.board.print(gameConfiguration.activeUnit)
        case _ => println("Move after game end")
      }

    }

    gameDone === true
    gameMoves.size === moveCount
  }
}
