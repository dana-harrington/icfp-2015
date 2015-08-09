package com.razorfish.icfp_2015.models

import com.razorfish.icfp_2015.Score

sealed trait GameConfiguration {
  def board: Board
  def score: Score
}

case class CompletedGameConfiguration(board: Board, score: Score) extends GameConfiguration

case class ActiveGameConfiguration( board: Board,
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

  def tryMoves(moves: Seq[GameMove]): Option[ActiveGameConfiguration] = {
    moves match {
      case Seq() =>
        Some(this)
      case move +: moreMoves =>
        if (tryMove(move).isDefined) {
          update(move) match {
            case ac: ActiveGameConfiguration =>
              ac.tryMoves(moreMoves)
            case cg: CompletedGameConfiguration =>
              None
          }
        } else {
          None
        }
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
   * @return points scored by freezing the current active unit
   */
  case class FreezeResult(score: Score, linesCleared: Int, board: Board)

  def freezeResult: FreezeResult = {
    val (linesCleared, newBoard) = board.freeze(activeUnit).filledRows
    val points = activeUnit.members.size + 100 * (1 + linesCleared) * linesCleared / 2
    val lineBonus =
      if (linesClearedWithPreviousUnit > 0) {
        Math.floor((linesClearedWithPreviousUnit - 1) * points / 10).toInt
      } else {
        0
      }
    FreezeResult(points + lineBonus, linesCleared, newBoard)
  }
  /**
   * Side-effecting!! Pulls next unit from source
   * @return configuration with current unit frozen, new unit activated if available
   */
  def freeze(): GameConfiguration = {
    val freezeState = freezeResult
    if (source.hasNext) {

      val newActiveUnit = source.next().center(board)
      newActiveUnit.fold[GameConfiguration] {

        //Can't place new unit
        CompletedGameConfiguration(board.freeze(activeUnit), score + freezeState.score)

      }{ activeUnit =>

        ActiveGameConfiguration(
          board = freezeState.board,
          activeUnit,
          source,
          score + freezeState.score,
          freezeState.linesCleared
        )
      }
    } else {
      CompletedGameConfiguration(board.freeze(activeUnit), score + freezeState.score)
    }
  }

}

object GameConfiguration {
  /**
   *
   * @param board
   * @param source
   * @return initial configuration with given board and unit source
   */
  def apply(board: Board, source: Source): GameConfiguration = {
    source.next().center(board).fold[GameConfiguration]{
      CompletedGameConfiguration(board, 0)
    } { startUnit =>
      ActiveGameConfiguration(board, startUnit, source, 0, 0)
    }
  }
}
