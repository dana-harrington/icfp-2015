package com.razorfish.icfp_2015.strategies

import java.io.File

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

import com.razorfish.icfp_2015.{Config, GameExecution, MoveEncoder}
import com.razorfish.icfp_2015.json.{Output, Parse}
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

  def specOutput(output: Output, parse: Parse, seed: Long, isDebug: Boolean): MatchResult[_] = {
    val gameMoves = solutionToGameMoves(output.solution)
    val source = new UnitSource(seed, parse.gameUnits, parse.sourceLength)

    var gameConfiguration = GameConfiguration(parse.board, source).asInstanceOf[ActiveGameConfiguration]
    var gameDone = false

    if (isDebug) {
      println(s"Game with ${gameMoves.size} moves")
      println(output.solution)
      println("Starting position:")
      gameConfiguration.board.print(gameConfiguration.activeUnit)
    }

    var moveCount = 0
    for (move <- gameMoves) {

      gameConfiguration.update(move) match {
        case _: CompletedGameConfiguration =>
          moveCount += 1
          if (isDebug) println(s"Move #$moveCount: ${move}: Game over")
          gameDone = true
        case gc: ActiveGameConfiguration if !gameDone => gameConfiguration = gc
          moveCount += 1
          if (isDebug) {
            println(s"Move #$moveCount: ${move}")
            gameConfiguration.board.print(gameConfiguration.activeUnit)
          }
        case _ => if (isDebug) println("Move after game end")
      }

    }

    gameDone === true
    gameMoves.size === moveCount
  }

  def spec(file: File, strategy: Strategy, config: Config, isDebug: Boolean = true): MatchResult[_] = {
    val parse = Parse(file)
    val seed = parse.sourceSeeds.head
    val gameExecution = new GameExecution(strategy, parse, seed, config.timeLimit, config.memoryLimit, config.powerPhrases)
    val output = Await.result(gameExecution.run, 20 seconds)
    specOutput(output, parse, seed, isDebug)
  }
}

class StrategySpecSpec extends StrategySpec {

  val icfpValidOutput = Output(6, 0, None, """
iiiiiiiimmiiiiiimimmiiiimimimmimimimimmimimimeemimeeeemimim
imimiiiiiimmeemimimimimiimimimmeemimimimmeeeemimimimmiiiiii
pmiimimimeeemmimimmemimimimiiiiiimeeemimimimimeeemimimimmii
iimemimimmiiiipimeeemimimmiiiippmeeeeemimimimiiiimmimimeemi
mimeeeemimimiiiipmeeemmimmiimimmmimimeemimimimmeeemimiiiiip
miiiimmeeemimimiiiipmmiipmmimmiippimemimeeeemimmiipppmeeeee
mimimmiimipmeeeemimimiimmeeeeemimmeemimmeeeemimiiippmiippmi
iimmiimimmmmmeeeemimmiippimmimimeemimimimmeemimimimmeemimim
imiimimimeeemmimimmmiiiiipimeemimimimmiiiimimmiiiiiiiimiimi
mimimeeemmimimimmiiiiiimimmemimimimimmimimimeemimiiiiiiiimi
iiimimimiimimimmimmimimimimmeeeemimimimimmmimimimimeemimimi
mimmmemimimmiiiiiiimiimimimmiiiiiimeeeeemimimimimmimimimmmm
emimimmeeeemimimimmiimimimmiiiiiipmeeeeemimimimimmiiiiimmem
imimimimmmmimimmeeeemimimimimeeemimimimmiimimimeeemmimimmii
iiiiimimiiiiiimimmiiiiiiiimmimimimimiiiimimimeemimimimimmee
emimimimimiiiiiiimiiiimimmemimimimmeemimimimeeemmimimmiiiii
immiiiipmmiiimmmimimeemimimeeemmimmiiiippmiiiimiiippimiimim
eemimimeeeemimimiiiipmeemimimiimiimimmimeeemimimmippipmmiim
emimmipimeeeemimmeemimiippimeeeeemimimmmimmmeeeemimimiiipim
miipmemimmeeeemimimiipipimmipppimeeemimmpppmmpmeeeeemimmemm""")

  "strategySpec" should {
    "process the entire supplied solution" in {

      val file = new File("src/test/resources/problems/problem_6.json")
      val config = Config(Seq(file), Set("Ei!"), None, None)

      specOutput(icfpValidOutput, Parse(file), 0, false)
    }


  }

}