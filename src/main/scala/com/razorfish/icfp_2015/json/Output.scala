package com.razorfish.icfp_2015.json

import play.api.libs.json.Json

import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

case class Output(
                   /* The `id` of the game configuration */
                   problemId: Int,
                   /* The seed for the particular game */
                   seed: Long,
                   /* A tag for this solution. */
                   tag: Option[String],
                   solution: String
                   ) {

  def submit: Try[Unit] = {

    val apiToken = ""
    val teamId = 999

    val builder = new com.ning.http.client.AsyncHttpClientConfig.Builder()
    val wsClient = new play.libs.ws.ning.NingWSClient(builder.build())

    val ws = wsClient
      .url(s"https://davar.icfpcontest.org/teams/$teamId/solutions")
      .setAuth("", apiToken)
      .setContentType("application/json")

    val response = ws.post(Json.stringify(Json.toJson(this)(Output.writes))).get(20, SECONDS)

    response.getStatus match {
      case 200 => Success(Unit)
      case _ => Failure(new Exception(response.getStatus + " (" + response.getStatusText + "): " + response.getBody))
    }
  }

}

object Output {
  implicit val writes = Json.writes[Output]
}
