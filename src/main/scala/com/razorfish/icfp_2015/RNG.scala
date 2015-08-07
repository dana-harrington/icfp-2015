package com.razorfish.icfp_2015

trait RNG extends Iterable[Long]

class RNGProvider(modulus: Long, multiplier: Long, increment: Long) {

  def fromSeed(seed: Long): RNG = ???

}
