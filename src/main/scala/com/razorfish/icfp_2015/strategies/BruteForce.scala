package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015._
import com.razorfish.icfp_2015.models.{GameMove, ActiveGameConfiguration}



case class BruteForceUnitAtATimeStrategy( moveEncoder: MoveEncoder,
                                          phrases: Set[PowerPhrase]) extends SteppedStrategy[Seq[GameMove]] {

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
        .filterNot(path => gc.activeUnit.unit.containsCycle(path.moves))
        .map { path =>
      (path, Path.scorePath(path, moveEncoder, phrases))
    }.maxBy(_._2)
    (bestPathAndScore._1.moves, bestPathAndScore._2)
  }

}
