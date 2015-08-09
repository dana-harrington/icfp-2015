package com.razorfish.icfp_2015

object PowerPhrase {
  def scoreMoves(moves: CharSequence, phrases: Set[PowerPhrase]): Score = {
    phrases.foldRight(0) {
      case (phrase, score) =>
        val occurrences = phrase.r.findAllMatchIn(moves).length
        score + phraseOfPowerScore(phrase, occurrences)
    }
  }

  def phraseOfPowerScore(phraseOfPower: PowerPhrase, occurrences: Int) = {
    val lenp = phraseOfPower.length
    val repsp = occurrences
    val power_bonusp = if (occurrences > 0) 300 else 0

    2 * lenp * repsp + power_bonusp
  }

}
