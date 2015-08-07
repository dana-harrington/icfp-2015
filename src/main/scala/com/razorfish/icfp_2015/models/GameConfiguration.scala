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

  /**
   *
   * @param move
   * @return if unit can move as requested the new state, otherwise None
   */
  def tryMove(move: GameMove): Option[GameUnit] = {
    val updatedActiveUnit = activeUnit.move(move)
    if (updatedActiveUnit.members.forall(cell => board.tileState(cell) == EmptyTile)) {
      Some(updatedActiveUnit)
    } else {
      None
    }
  }

  /**
   *
   * @param move
   * @return Game configuration after move is applied
   */
  def update(move: GameMove): GameConfiguration = {
    tryMove(move) match {
      case Some(updatedUnit) => this.copy(activeUnit = updatedUnit)
      case None => freeze
    }
  }

  /**
   *
   * @return configuration with current unit frozen, new unit activated if available
   */
  def freeze: GameConfiguration = {
    val (linesCleared, newBoard) = board.freeze(activeUnit).filledRows
    val points = activeUnit.members.size + 100 * (1 + linesCleared) * linesCleared / 2
    val lineBonus =
      if (linesClearedWithPreviousUnit > 0) {
        Math.floor((linesClearedWithPreviousUnit - 1) * points / 10)
      } else {
        0
      }
    val moveScore = points + lineBonus
    if (source.hasNext) {

      val activeUnit = source.next()
      //TODO: reposition to center of board

      GameConfigurationImpl(
        board = newBoard,
        activeUnit,
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
  /**
   *
   * @param board
   * @param source
   * @return initial configuration with given board and unit source
   */
  def apply(board: Board, source: Source): GameConfiguration = {
    GameConfigurationImpl(board, source.next(), source, 0, 0)
  }
}
