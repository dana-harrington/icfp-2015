package com.razorfish.icfp_2015.strategies

import com.razorfish.icfp_2015.MoveEncoder
import com.razorfish.icfp_2015.models.ActiveGameConfiguration

/**
 * Considers only the game, not the phrases to pick moves. Then attempts to optimize the encoding
 * @param ai
 * @param moveEncoder
 */
case class PhraseAfterthoughtStrategy(ai: MoveAI, moveEncoder: MoveEncoder, phrases: Set[String]) extends SteppedStrategy[Unit] {
  def initialState = ()
  def step(gc: ActiveGameConfiguration, state: Unit) = {
    (ai.step(gc), ())
  }
}
