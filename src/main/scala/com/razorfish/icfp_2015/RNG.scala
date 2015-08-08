package com.razorfish.icfp_2015

trait RNG extends Iterable[Long]

class RNGProvider(modulus: Long, multiplier: Long, increment: Long, seed: Long) extends Iterator[Int] {

  private var currentSeed = seed

  def hasNext: Boolean = true

  def next: Int = {
    currentSeed = (multiplier * currentSeed + increment) % modulus

    val bits = currentSeed.toBinaryString
    def fillZeros(b: String): String = if (b.length < 32) fillZeros("0" + b) else b

    Integer.parseInt(fillZeros(bits).takeRight(31).take(15), 2)
  }

}