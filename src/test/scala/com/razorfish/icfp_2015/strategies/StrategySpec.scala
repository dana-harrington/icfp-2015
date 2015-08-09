package com.razorfish.icfp_2015.strategies

import java.io.File

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

import com.razorfish.icfp_2015.{Score, Config, GameExecution, MoveEncoder}
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

  def specOutput(output: Output, parse: Parse, seed: Long, expectedScore: Option[Score], isDebug: Boolean): MatchResult[_] = {
    val gameMoves = solutionToGameMoves(output.solution)
    val source = new UnitSource(seed, parse.gameUnits, parse.sourceLength)

    var gameConfiguration = GameConfiguration(parse.board, source).asInstanceOf[ActiveGameConfiguration]
    var completedGame: Option[CompletedGameConfiguration] = None

    if (isDebug) {
      println(s"Game with ${gameMoves.size} moves")
      println(s"[${output.solution}]")
      println("Starting position:")
      gameConfiguration.board.print(gameConfiguration.activeUnit)
    }

    var moveCount = 0
    for (move <- gameMoves) {

      gameConfiguration.update(move) match {
        case cgc: CompletedGameConfiguration =>
          moveCount += 1
          if (isDebug) println(s"Move #$moveCount: ${move}: Game over   Score: ${cgc.score}")
          completedGame = Some(cgc)
        case gc: ActiveGameConfiguration if completedGame.isEmpty => gameConfiguration = gc
          moveCount += 1
          if (isDebug) {
            println(s"Move #$moveCount: ${move}")
            gameConfiguration.board.print(gameConfiguration.activeUnit)
          }
        case _ => if (isDebug) println("Move after game end")
      }

    }

    completedGame.isDefined === true
    if (expectedScore.isDefined) completedGame.get.score === expectedScore.get
    gameMoves.size === moveCount
  }

  def spec(file: File, strategy: Strategy, config: Config, expectedScore: Option[Score] = None, isDebug: Boolean = true): MatchResult[_] = {
    val parse = Parse(file)
    val seed = parse.sourceSeeds.head
    val gameExecution = new GameExecution(strategy, parse, seed, None, config.timeLimit, config.memoryLimit, config.powerPhrases)
    val output = Await.result(gameExecution.run, 20 seconds)
    specOutput(output, parse, seed, expectedScore, isDebug)
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

  val submittedSolution0 = Output(0, 0, None, """
ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei! ei""")

  "strategySpec" should {
    "process the entire ICFP supplied solution" in {

      val file = new File("src/test/resources/problems/problem_6.json")
      val config = Config(Seq(file), Set("Ei!"), None, None)
      val expectedScore = Some(3261L)

      specOutput(icfpValidOutput, Parse(file), 0, expectedScore, false)
    }

    "process our submitted solution" in {
      val file = new File("src/test/resources/problems/problem_0.json")
      val config = Config(Seq(file), Set("Ei!"), None, None)
      val expectedScore = None //TODO: calculate correct score Some(431L)

      specOutput(submittedSolution0, Parse(file), 0, expectedScore, false)
    }
  }

}