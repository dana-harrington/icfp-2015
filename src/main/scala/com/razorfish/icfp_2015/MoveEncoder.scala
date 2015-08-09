package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models._

import MoveEncoder._

case class EncodedMoves(moves: Seq[Char], powerWordScore: Score)

sealed trait MatchState

case class NodeState(count: Int, powerWords: Seq[PowerPhrase]) extends MatchState

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
    val result = moves.foldLeft[(Vector[Char],NodeState)]((Vector(), NodeState(0,powerPhrases))) { (acc, move) =>
      val moveVector = movesCode(move)
      val encoded = acc._1 :+ movesCode(move).head
      val matches = partialMatches(moveVector, acc._2.count, acc._2.powerWords)
      val rematched = {
        if (matches.nonEmpty) matches
        else partialMatches(moveVector, 0, acc._2.powerWords)
      }
      val matchState = NodeState(
          count = if (rematched.nonEmpty) acc._2.count + 1 else 0,
          powerWords = if (rematched.nonEmpty) rematched else powerPhrases
        )
      val nextRound = (encoded, matchState)
      choosePowerWord(matchState).fold(nextRound){ powerWord =>
        val replaceable = encoded.takeRight(acc._2.count+1)
        val replacement = powerWord ++ replaceable.drop(powerWord.length)
        (encoded.dropRight(acc._2.count+1) ++ replacement, NodeState(0,powerPhrases))
      }
    }
    EncodedMoves(result._1.foldLeft("")(_+_), 0L)
  }

  def partialMatches(moveVect: Vector[Char], position: Int, powerWords: Seq[PowerPhrase]): Seq[PowerPhrase] = {
    val longPowerWords = powerWords.filter(_.length > position).filter { word =>
      moveVect contains word(position).toLower
    }
    val shortPowerWords = powerWords.filter(_.length <= position)
    longPowerWords ++ shortPowerWords
  }

  def choosePowerWord(state: NodeState): Option[PowerPhrase] = {
    state.powerWords.find { word =>
      val foundWord = word.length <= state.count && state.powerWords.forall(_.length <= word.length)
      if(foundWord) println(state.powerWords + "\n\n")
      foundWord
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

  val phrasesOfPower = Set("ei!")

  type PowerWord = String
  type PowerPhrase = Vector[Char]

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
        encodings.map(_ -> move)
    }
  }
}
