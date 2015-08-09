package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.MoveEncoder.PowerWord
import com.razorfish.icfp_2015.{Score, MoveEncoder}
import com.razorfish.icfp_2015.models.{CompletedGameConfiguration, GameMove, ActiveGameConfiguration}



case class BruteForceUnitAtATimeStrategy( moveEncoder: MoveEncoder,
                                          phrases: Set[PowerWord]) extends SteppedStrategy[Seq[GameMove]] {

  def initialState = Seq.empty[GameMove]

  // if we have saved up moves use the next one, otherwise calculate the moves of the next unit
  def step(gc: ActiveGameConfiguration, precomputedMoves: Seq[GameMove]): (GameMove, Seq[GameMove]) = {
    precomputedMoves match {
      case move +: moves =>
        (move, moves)
      case Seq() =>
        val unitMoves = solveForCurrentUnit(gc)._1
        (unitMoves.head, unitMoves.tail)
    }
  }

  // Find the highest scoring sequence of moves for a unit by brute force
  def solveForCurrentUnit(gc: ActiveGameConfiguration): (List[GameMove], Score) = {
    val bestPathAndScore =
      Path
        .allPaths(gc)
        .filterNot(path => gc.activeUnit.containsCycle(path.moves))
        .map { path =>
      (path, Path.scorePath(path, moveEncoder, phrases))
    }.maxBy(_._2)
    (bestPathAndScore._1.moves, bestPathAndScore._2)
  }

}

case class Path(moves: List[GameMove], score: Score)

object Path {
  /**
   * All paths that end is a frozen piece
   * @param gc
   * @return
   */
  def allPaths(gc: ActiveGameConfiguration): Set[Path] = {
    for {
      move <- GameMove.moves
      path <- gc.tryMove(move) match {
        case None =>
          val score = gc.freezeResult.score
          Set(Path(List(move), gc.freezeResult.score))
        case Some(_) =>
          gc.update(move) match {
            case gc: ActiveGameConfiguration =>
              allPaths(gc)
            case cg: CompletedGameConfiguration =>
              throw new Exception("Not freezing move resulted in completed game")
          }
      }
    } yield {
      Path(move :: path.moves, path.score)
    }
  }

  def scorePath(path: Path, moveEncoder: MoveEncoder, phrases: Set[String]): Score = {
    moveEncoder.encode(path.moves, phrases).powerWordScore
  }

}