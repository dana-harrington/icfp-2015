package com.razorfish.icfp_2015.json

import play.api.libs.json.Json

case class Output(
                   /* The `id` of the game configuration */
                   problemId: Int,
                   /* The seed for the particular game */
                   seed: Long,
                   /* A tag for this solution. */
                   tag: String,
                   solution: String
)

object Output {
  implicit val format = Json.format[Output]
}
