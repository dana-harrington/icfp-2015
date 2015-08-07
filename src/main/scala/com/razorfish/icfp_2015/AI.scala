package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models.{SW, Source, GameMove, GameConfigurationImpl}

trait AI {
  def step(gc: GameConfigurationImpl): GameMove
}

object ReallyStupidAI extends AI {
  def step(gc: GameConfigurationImpl): GameMove = {
    SW
  }
}
