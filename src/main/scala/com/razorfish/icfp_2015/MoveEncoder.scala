package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models._

import MoveEncoder._

case class EncodedMoves(moves: Seq[Char], powerWordScore: Score)

sealed trait MatchState

case class NodeState(index: Int, powerWords: Seq[PowerMove]) extends MatchState

import com.razorfish.icfp_2015.strategies.POPStrategy

trait MoveEncoder {
  def encode(moves: Seq[GameMove], powerWords: Set[PowerWord]): EncodedMoves
}

object InLineEncoder extends MoveEncoder {

  def simpleEncode(moves: Seq[GameMove]): EncodedMoves = {
    val simpleCode = moves.map(movesCode(_).head)
    EncodedMoves(simpleCode, 0L)
  }

  def encode(moves: Seq[GameMove], powerWords: Set[PowerWord]): EncodedMoves = {
    val powerPhrases = powerWords.toSeq.map(_.toVector)
    val result = moves.zipWithIndex.foldLeft[(Vector[Char], NodeState)]((Vector(), NodeState(0,powerPhrases))) { case (acc, (move, i)) =>
      val moveVector = movesCode(move)
      val encoded = acc._1 :+ movesCode(move).head
      val matches = partialMatches(moveVector, acc._2.index, acc._2.powerWords)
      val rematched = {
        if (matches.nonEmpty) matches
        else partialMatches(moveVector, 0, acc._2.powerWords)
      }
      val matchState = NodeState(
          index = if (rematched.nonEmpty) acc._2.index + 1 else 0,
          powerWords = if (rematched.nonEmpty) rematched else powerPhrases
        )
      val nextRound = (encoded, matchState)

      choosePowerWord(matchState).fold(nextRound){ powerWord =>
        val possibleReplace = encoded.takeRight(acc._2.index+1)
        val willReplace = possibleReplace.drop(powerWord.length)
        val replacement = powerWord ++ willReplace
        val missedPhrases = if (willReplace.nonEmpty) {
          val missedMoves = moves.slice(1+i-willReplace.length,1+i)
          stateForMoves(missedMoves, powerPhrases)
        } else Seq()
        val newPhrases = if (missedPhrases.nonEmpty) missedPhrases else powerPhrases
        val newStateIndex = if (missedPhrases.nonEmpty) willReplace.length else 0
        (encoded.dropRight(acc._2.index+1) ++ replacement, NodeState(newStateIndex,newPhrases))
      }
    }
    val finalPhraseScore = powerPhrases.map { phrase =>
      val count = phrase.foldLeft("")(_+_).r.findAllIn(result._1).length
      2 * phrase.length * count + (if (count > 0) 300 else 0)
    }.sum
    EncodedMoves(result._1.foldLeft("")(_+_), finalPhraseScore)
  }

  def stateForMoves(moves: Seq[GameMove], powerPhrases: Seq[PowerMove]): Seq[PowerMove] = {
    moves.zipWithIndex.foldLeft[Seq[PowerMove]](powerPhrases){ case (acc, (move, i)) =>
      val moveVector = movesCode(move)
      partialMatches(moveVector ,i, acc)
    }
  }


  def partialMatches(moveVect: Vector[Char], index: Int, powerPhrases: Seq[PowerMove]): Seq[PowerMove] = {
    val longPowerWords = powerPhrases.filter(_.length > index).filter { word =>
      moveVect contains word(index).toLower
    }
    val shortPowerWords = powerPhrases.filter(_.length <= index)
    longPowerWords ++ shortPowerWords
  }

  def choosePowerWord(state: NodeState): Option[PowerMove] = {
    state.powerWords.find { word =>
      word.length <= state.index && state.powerWords.forall(_.length <= word.length)
    }
  }
}

object DumbEncoder extends MoveEncoder {
  def encode(moves: Seq[GameMove],  powerWords: Set[PowerWord] = Set.empty): EncodedMoves = {
    val encodedMoves = moves.map{ move =>
      MoveEncoder.moveEncodings(move).head
    }
    EncodedMoves(encodedMoves, 0)
  }
}

object MoveEncoder {

  val phrasesOfPower = Set("ei!", "yuggoth")

  type PowerWord = String
  type PowerMove = Vector[Char]

  lazy val movesCode: Map[GameMove, Vector[Char]] = Map(
    W -> "p'!.03".toVector,
    E -> "bcefy2".toVector,
    SW -> "aghij4".toVector,
    SE -> "lmno 5".toVector,
    CW -> "dqrvz1".toVector,
    CCW -> "kstuwx".toVector
  )

  val moveEncodings: Map[GameMove, Set[Char]] = Map(
    W ->
      Set('p', '\'', '!', '.', '0', '3'),
    SW ->
      Set('a','g','h','i','j','4'),
    SE ->
      Set('l','m','n','o',' ', '5'),
    E ->
      Set('b','c','e','f','y'),
    CW ->
      Set('d','q','r','v','z','l'),
    CCW ->
      Set('k','s','t','u','w','x')
  )

  val decodeMove: Map[Char, GameMove] = {
    MoveEncoder.moveEncodings.flatMap {
      case (move, encodings) =>
        encodings.map(_.toLower -> move) ++ encodings.map(_.toUpper -> move)
    }
  }

  def decodeMoves(moves: Seq[Char]): Seq[GameMove] = {
    moves.flatMap { em =>
      decodeMove.get(em)
    }
  }
}
