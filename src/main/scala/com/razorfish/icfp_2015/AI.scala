package com.razorfish.icfp_2015

import com.razorfish.icfp_2015.models.{Source, GameMove, GameConfiguration}

trait AI {
  def step(gc: GameConfiguration, source: Source): GameMove = ???
}
