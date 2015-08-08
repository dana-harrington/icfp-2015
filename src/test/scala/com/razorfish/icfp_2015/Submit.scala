package com.razorfish.icfp_2015

import play.api.libs.json.Json

import com.fasterxml.jackson.databind.ObjectMapper
import com.razorfish.icfp_2015.json.Output

import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object Submit {

  val apiToken = "ttrd/Lf4+K4MgNvfhXTSqTaz0GxROvdJw5wzOo+78Lc="
  val teamId = 294

  def submit(output: Seq[Output]): Try[Unit] = {

    import Output._

    val builder = new com.ning.http.client.AsyncHttpClientConfig.Builder()
    val wsClient = new play.libs.ws.ning.NingWSClient(builder.build())

    val mapper = new ObjectMapper();
    val json = mapper.readTree(Json.stringify(Json.toJson(output)))

    val ws = wsClient
      .url(s"https://davar.icfpcontest.org/teams/$teamId/solutions")
      .setAuth(":" + apiToken)
      .setContentType("application/json")
      .setBody(json)

    val response = ws.post(json).get(20, SECONDS)

    response.getStatus match {
      case 201 => Success(Unit)
      case _ =>
        val error = response.getStatus + " (" + response.getStatusText + "): " + response.getBody
        println(error)
        Failure(new Exception(error))
    }
  }
}
