package com.razorfish.icfp_2015.strategies

import java.io.File

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

import com.razorfish.icfp_2015._
import com.razorfish.icfp_2015.json.{ParseSpec, Output, Parse}
import com.razorfish.icfp_2015.models._
import scala.concurrent.duration._

import scala.concurrent.Await

trait StrategySpec extends Specification {

  def getMove(move: EncodedMove): Option[GameMove] = {
    val c = move.toLower
    MoveEncoder.moveEncodings.mapValues(_.contains(c)).filter {
      case (k, v) => v
    }.headOption.map(_._1)
  }

  def solutionToGameMoves(solution: String): Seq[GameMove] = {
    solution.toCharArray.flatMap(getMove(_)).toSeq
  }

  def specOutput(output: Output, parse: Parse, seed: Long, expectedScore: Option[Score], isDebug: Boolean, phrases: Set[PowerPhrase]): MatchResult[_] = {
    val gameMoves = solutionToGameMoves(output.solution)
    val source = new UnitSource(seed, parse.gameUnits, parse.sourceLength).toSeq

    var gameConfiguration = GameConfiguration(parse.board, source).asInstanceOf[ActiveGameConfiguration]
    var completedGame: Option[CompletedGameConfiguration] = None

    if (isDebug) {
      println(s"Game with ${gameMoves.size} moves")
      println(s"[${output.solution}]")
      println("Starting position:")
      gameConfiguration.board.print(gameConfiguration.activeUnit.unit)
    }

    var moveCount = 0
    var score: Option[Long] = None
    for (move <- gameMoves) {

      gameConfiguration.update(move) match {
        case cgc: CompletedGameConfiguration =>
          moveCount += 1
          score = Some(cgc.score + PowerPhrase.scoreMoves(output.solution, phrases))
          if (isDebug) println(s"Move #$moveCount: ${move}: Game over   Score: ${score.get}")
          completedGame = Some(cgc)
        case gc: ActiveGameConfiguration if completedGame.isEmpty => gameConfiguration = gc
          moveCount += 1
          if (isDebug) {
            println(s"Move #$moveCount: ${move}")
            gameConfiguration.board.print(gameConfiguration.activeUnit.unit)
          }
        case _ => if (isDebug) println("Move after game end")
      }

    }

    completedGame.isDefined === true
    if (expectedScore.isDefined) score.get === expectedScore.get
    gameMoves.size === moveCount
  }

  def spec(file: File, strategy: StrategyBuilder, config: Config, phrases: Set[PowerPhrase], expectedScore: Option[Score] = None, isDebug: Boolean = true): MatchResult[_] = {
    val parse = Parse(file)
    val seed = parse.sourceSeeds.head
    val gameExecution = new GameExecution(strategy, parse, seed, None, config.timeLimit, config.memoryLimit, config.powerPhrases)
    val output = Await.result(gameExecution.run, 5 minutes)
    specOutput(output, parse, seed, expectedScore, isDebug, phrases)
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
    "process the entire ICFP supplied solution" in {
      val file = new File("src/test/resources/problems/problem_6.json")
      val expectedScore = Some(3261L)
      specOutput(icfpValidOutput, Parse(file), 0, expectedScore, false, MoveEncoder.phrasesOfPower)
    }

    "process our submitted solution" in {
      val submittedSolution0 = Output(0, 0, None, "ei! ei! ei! ei! ei! ei! ei! ei! ei! ei!ei! ei! ei! ei! ei! ei! ei! ei! ei! ei")

      val file = new File("src/test/resources/problems/problem_0.json")
      val config = Config(Seq(file), MoveEncoder.phrasesOfPower, None, None)
      val expectedScore = Some(431L)
      val input = Parse(file)
      val startGC = GameConfiguration(
        input.board,
        new UnitSource(input.sourceSeeds.head, input.gameUnits, input.sourceLength).toSeq)
        .asInstanceOf[ActiveGameConfiguration]
      val moves = MoveEncoder.decodeMoves(submittedSolution0.solution)
      val result = moves.foldLeft[GameConfiguration](startGC) {
        case (gc, move) =>
          gc.asInstanceOf[ActiveGameConfiguration].update(move)
      }
      val unitScore = result.score
      val powerPhraseScore = PowerPhrase.scoreMoves(submittedSolution0.solution, MoveEncoder.phrasesOfPower)

      specOutput(submittedSolution0, Parse(file), 0, expectedScore, false, MoveEncoder.phrasesOfPower)
    }
  }
  "scoring" should {
    "score iestrategy" in {
      val solution = Output(15, 0, None, "ei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!ei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!ei!lei!lei!lei!lei!lei!lei!ei!lei!")
      val file = ParseSpec.problems(solution.problemId)
      val expectedScore = 560
      specOutput(solution, Parse(file), 0, Some(expectedScore), false, MoveEncoder.phrasesOfPower)
    }

    "score pop iestrategy" in {
      val solution = Output(15, 0, None, "ei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!l")
      val file = ParseSpec.problems(solution.problemId)
      val expectedScore = 552
      specOutput(solution, Parse(file), 0, Some(expectedScore), false, MoveEncoder.phrasesOfPower)
    }

    "score pop #1" in {
      val solution = Output(15, 0, None, "ia! ia!lia! ia!lia! ia!lia! ia!lia! ia!l")
      val file = ParseSpec.problems(solution.problemId)
      val expectedScore = 390
      specOutput(solution, Parse(file), 0, Some(expectedScore), false, MoveEncoder.phrasesOfPower)
    }

    "score pop #3" in {
      val solution = Output(15, 0, None, "yuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothyuggothb")
      val file = ParseSpec.problems(solution.problemId)
      val expectedScore = 506
      specOutput(solution, Parse(file), 0, Some(expectedScore), false, MoveEncoder.phrasesOfPower)
    }

    "score pop #4" in {
      val solution = Output(15, 0, None, "ei!r'lyehr'lyehr'lyehr'lyehr'lyehr'lyehdplbbbapr'lyehr'lyehr'lyehr'lyehr'lyehei!r'lyehr'lyehr'lyehr'lyehpr'lyehr'lyehdplbapppppppei!ppppppppppppppp")
      val file = ParseSpec.problems(solution.problemId)
      val expectedScore = 858
      specOutput(solution, Parse(file), 0, Some(expectedScore), false, MoveEncoder.phrasesOfPower)
    }

    "score pop branch #1" in {
      val solution = Output(15, 0, None, "ei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!lei!llllei!lei!lei!lei!lei!lei!lei!lllei!lei!lei!lei!lei!lei!llei!lei!lei!lei!lei!lei!lei!lei!llllei!lei!lei!lllei!lei!llei!llllll")
      val file = ParseSpec.problems(solution.problemId)
      val expectedScore = 602
      specOutput(solution, Parse(file), 0, None /*Some(expectedScore)*/, false, MoveEncoder.phrasesOfPower)
    }

    "score pop branch #1" in {
      val solution = Output(15, 0, None, "yuggothyuggothyuggothyuggothlyuggothyuggothyuggothei!lyuggothyuggothr'lyehlei!llei!lyuggothyuggothr'lyehlei!lyuggothyuggothei!lyuggothlyuggothlyuggothlei!llllll")
      val file = ParseSpec.problems(solution.problemId)
      val expectedScore = 1224
      specOutput(solution, Parse(file), 0, None /*Some(expectedScore)*/, false, MoveEncoder.phrasesOfPower)
    }

  }

}