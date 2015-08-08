package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models._

import MoveEncoder._

case class EncodedMoves(moves: Seq[Char], powerWordScore: Score)

sealed trait MatchState

case class NodeState(count: Int, powerWords: Seq[PowerWord]) extends MatchState

trait MoveEncoder {
  def encode(moves: Seq[GameMove], powerWords: Seq[PowerWord]): EncodedMoves
}

object InLineEncoder extends MoveEncoder {

  def getMoveCodeHead(move: GameMove): Char =
    getMoveCodeSet(move).head

  def getMoveCodeSet(move: GameMove): Vector[Char] =
    commandCode(move)

  def simpleEncode(moves: Seq[GameMove]): EncodedMoves = {
    val simpleCode = moves.map(commandCode(_).head)
    EncodedMoves(simpleCode, 0L)
  }

  def encode(moves: Seq[GameMove], powerWords: Seq[PowerWord]): EncodedMoves = {

    val result = moves.foldLeft(("", NodeState(0,powerWords))) { (acc, move) =>

      val head = getMoveCodeHead(move)

      val moveVector = getMoveCodeSet(move)

      val encoded = acc._1 + head

      val matches = partialMatches(moveVector, acc._2.count, acc._2.powerWords)

      val rematched = if (matches.nonEmpty) matches
      else partialMatches(moveVector, 0, acc._2.powerWords)

      val nextMatchState = NodeState(
        count = if (rematched.nonEmpty) acc._2.count + 1 else 0,
        powerWords = if (rematched.nonEmpty) rematched else powerWords
      )

      val nextRound = (encoded, nextMatchState)

      choosePowerWord(nextMatchState).fold(nextRound){ powerWord =>
        (encoded.dropRight(powerWord.length) + powerWord.foldRight("")(_+_), NodeState(0,powerWords))
      }
    }
    EncodedMoves(result._1, 0L)
  }

  def partialMatches(moveSet: Vector[Char], position: Int, powerWords: Seq[PowerWord]): Seq[PowerWord] =
    powerWords.filter(_.length > position).filter { word =>
      moveSet contains word(position).toLower
    }

  def choosePowerWord(state: NodeState): Option[PowerWord] = {
    state.powerWords.find { word =>
      state.count == word.length
    }
  }
}

object DumbEncoder extends MoveEncoder {
  def encode(moves: Seq[GameMove],  powerWords: Seq[PowerWord] = Seq()): EncodedMoves = {
    val encodedMoves = moves.map{ move =>
      MoveEncoder.moveEncodings(move).head
    }
    EncodedMoves(encodedMoves, 0)
  }
}

object MoveEncoder {

  type PowerWord = Vector[Char]

  lazy val commandCode: Map[GameMove, Vector[Char]] = Map(
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
}
