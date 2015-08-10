package com.razorfish.icfp_2015.models

import com.razorfish.icfp_2015.Score

sealed trait GameConfiguration {
  def board: Board
  def score: Score
}

case class CompletedGameConfiguration(board: Board, score: Score) extends GameConfiguration
case class ActiveUnit(unit: GameUnit, positionHistory: Set[GameUnit], moveHistory: Seq[GameMove])
object ActiveUnit {
  def apply(unit: GameUnit): ActiveUnit = {
    ActiveUnit(unit, Set(unit), Seq.empty)
  }
}


sealed trait MoveResult {
  def toOption: Option[GameUnit]
}
case class MoveSuccess(unit: GameUnit) extends MoveResult {
  val toOption = Some(unit)
}
case object UnitFrozen extends MoveResult {
  val toOption = None
}
case object GameDisqualification extends MoveResult {
  val toOption = None
}

case class DisqualificationException(gc: ActiveGameConfiguration, gameMove: GameMove)
  extends Exception(s"Disqualified, unit move history ${gc.activeUnit.moveHistory} by move $gameMove")

case class ActiveGameConfiguration( board: Board,
                                  activeUnit: ActiveUnit,
                                  source: Source,
                                  score: Score,
                                  linesClearedWithPreviousUnit: Int) extends GameConfiguration {


  /**
   *
   * @param move
   * @return if unit can move as requested the new state, otherwise None
   */
  def tryMove(move: GameMove): MoveResult = {
    val updatedActiveUnit = activeUnit.unit.move(move)
    if (activeUnit.positionHistory contains updatedActiveUnit) {
      GameDisqualification
    } else if (updatedActiveUnit.members.forall(cell => board.tileState(cell) == EmptyTile)) {
      MoveSuccess(updatedActiveUnit)
    } else {
      UnitFrozen
    }
  }

  def tryMoves(moves: Seq[GameMove]): Option[ActiveGameConfiguration] = {
    moves match {
      case Seq() =>
        Some(this)
      case move +: moreMoves =>
        tryMove(move) match {
          case MoveSuccess(_) =>
            update(move) match {
              case ac: ActiveGameConfiguration =>
                ac.tryMoves(moreMoves)
              case cg: CompletedGameConfiguration =>
                None
            }
          case UnitFrozen =>
            None
          case GameDisqualification =>
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
      case MoveSuccess(updatedUnit) if activeUnit.positionHistory contains updatedUnit =>
        throw new Exception(s"Unit repeated position in ${activeUnit.moveHistory}")
      case MoveSuccess(updatedUnit) =>
        this.copy(activeUnit = ActiveUnit(
          unit = updatedUnit,
          positionHistory = activeUnit.positionHistory + updatedUnit,
          moveHistory = activeUnit.moveHistory :+ move))
      case UnitFrozen =>
        freeze()
      case GameDisqualification =>
        throw new DisqualificationException(this, move)
    }
  }

  /**
   *
   * @return points scored by freezing the current active unit
   */
  case class FreezeResult(score: Score, linesCleared: Int, board: Board)

  def freezeResult: FreezeResult = {
    val (linesCleared, newBoard) = board.freeze(activeUnit.unit).filledRows
    val points = activeUnit.unit.members.size + 100 * (1 + linesCleared) * linesCleared / 2
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
    if (!source.isEmpty) {

      val newActiveUnit = source.head.center(freezeState.board)
      newActiveUnit.fold[GameConfiguration] {

        //Can't place new unit
        CompletedGameConfiguration(board.freeze(activeUnit.unit), score + freezeState.score)

      }{ activeUnit =>

        ActiveGameConfiguration(
          board = freezeState.board,
          activeUnit = ActiveUnit(activeUnit),
          source.tail,
          score + freezeState.score,
          freezeState.linesCleared
        )
      }
    } else {
      CompletedGameConfiguration(board.freeze(activeUnit.unit), score + freezeState.score)
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
    source.head.center(board).fold[GameConfiguration]{
      CompletedGameConfiguration(board, 0)
    } { startUnit =>
      ActiveGameConfiguration(board, ActiveUnit(startUnit), source.tail, 0, 0)
    }
  }
}
