package com.razorfish.icfp_2015.models

import com.razorfish.icfp_2015.RNGProvider

class UnitSource(seed: Long, units: IndexedSeq[GameUnit], sourceLength: Int) extends Source {
  require(sourceLength >= 0)

  private val modulus = BigDecimal(2).pow(32).toLongExact
  private val multiplier = 1103515245
  private val increment = 12345

  private var remainingUnits = sourceLength
  private val numUnits = units.size

  private val rng = new RNGProvider(modulus, multiplier, increment, seed)

  override def hasNext: Boolean = remainingUnits > 0

  override def next(): GameUnit = {
    val idx = rng.next % numUnits
    remainingUnits -= 1
    units(idx)
  }
}