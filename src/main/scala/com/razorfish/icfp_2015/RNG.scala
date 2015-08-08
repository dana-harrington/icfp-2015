package com.razorfish.icfp_2015

trait RNG extends Iterable[Long]

class RNGProvider(modulus: Long, multiplier: Long, increment: Long, seed: Long) extends Iterator[Int] {

  private var currentSeed = seed

  def hasNext: Boolean = true

  def next: Int = {
    val result = currentSeed >> 16 & 0x7fff
    currentSeed = (multiplier * currentSeed + increment) % modulus
    result.toInt
  }

}