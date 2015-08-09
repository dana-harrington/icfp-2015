package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.{Score, MoveEncoder}
import com.razorfish.icfp_2015.models.{CompletedGameConfiguration, GameMove, ActiveGameConfiguration}

case class TrialState(moves: Int, score: Option[Int])

case class BruteForce( evaluator: TrialState => Double,
                       moveEncoder: MoveEncoder) extends SteppedStrategy[TrialState] {

  def initialState: TrialState = TrialState(0, None)

  var moves = List.empty[GameMove]

  def step(gc: ActiveGameConfiguration, state: TrialState): (GameMove, TrialState) = {
    if (moves.isEmpty) {
      moves = solveForCurrentUnit()
    }
    val nextMove = moves.head
    moves = moves.tail
    ???
  }

  def solveForCurrentUnit(): List[GameMove] = ???


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