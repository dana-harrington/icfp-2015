package com.razorfish.icfp_2015.models

import com.razorfish.icfp_2015.Score

sealed trait GameConfiguration {
  def board: Board
  def score: Score
}

case class GameDoneConfiguration(board: Board, score: Score) extends GameConfiguration

case class GameConfigurationImpl( board: Board,
                                  activeUnit: GameUnit,
                                  source: Iterator[GameUnit],
                                  score: Score,
                                  linesClearedWithPreviousUnit: Int) extends GameConfiguration {

  def update(move: GameMove): GameConfiguration = {
    val updatedActiveUnit = activeUnit.move(move)
    if (updatedActiveUnit.positions.forall(cell => board.tileState(cell) == EmptyTile)) {
      copy(activeUnit = updatedActiveUnit, source = source)
    } else {
      freeze
    }
  }

  def freeze: GameConfiguration = {
    val newBoard = board.freeze(activeUnit)
    val linesCleared = 0 // TODO: detect lines cleared
    val points = activeUnit.positions.size + 100 * (1 + linesCleared) * linesCleared / 2
    val lineBonus =
      if (linesClearedWithPreviousUnit > 0) {
        Math.floor((linesClearedWithPreviousUnit - 1) * points / 10)
      } else {
        0
      }
    val moveScore = points + lineBonus
    if (source.hasNext) {
      GameConfigurationImpl(
        board = newBoard,
        activeUnit = source.next(),
        source,
        score,
        linesCleared
      )
    } else {
      GameDoneConfiguration(board.freeze(activeUnit), score)
    }
  }

  // TODO: detect and remove full rows


}

object GameConfiguration {
  def apply(board: Board, source: Source): GameConfiguration = {
    GameConfigurationImpl(board, source.next(), source, 0, 0)
  }
}
