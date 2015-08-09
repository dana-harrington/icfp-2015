package com.razorfish.icfp_2015

object PowerPhrase {
  def scoreMoves(moves: CharSequence, phrases: Set[String]): Score = {
    phrases.foldRight(0) {
      case (phrase, score) =>
        val occurrences = phrase.r.findAllMatchIn(moves).length
        phraseOfPowerScore(phrase, occurrences)
    }
  }

  def phraseOfPowerScore(phraseOfPower: String, occurrences: Int) = {
    val lenp = phraseOfPower.length
    val repsp = occurrences
    val power_bonusp = if (occurrences > 0) 300 else 0

    2 * lenp * repsp + power_bonusp
  }

}
