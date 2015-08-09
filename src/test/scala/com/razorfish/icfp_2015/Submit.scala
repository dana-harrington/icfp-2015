package com.razorfish.icfp_2015

import java.util.Date

import play.api.libs.json.Json

import com.fasterxml.jackson.databind.ObjectMapper
import com.razorfish.icfp_2015.json.{Parse, Output, ParseSpec}
import com.razorfish.icfp_2015.strategies.Strategy

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

object Submit {

  val phrasesOfPower = Set("ei!")

  val apiToken = "ttrd/Lf4+K4MgNvfhXTSqTaz0GxROvdJw5wzOo+78Lc="
  val teamId = 294

  def generateTag(strategyName: String): Option[String] =
    Some(strategyName + "-" + (new Date).toString)

  def submit(output: Seq[Output]): Try[Unit] = {

    import Output._

    val builder = new com.ning.http.client.AsyncHttpClientConfig.Builder()
    val wsClient = new play.libs.ws.ning.NingWSClient(builder.build())

    val mapper = new ObjectMapper()
    val bodyString = Json.stringify(Json.toJson(output))
    val json = mapper.readTree(bodyString)

    println("-------------------------------------------------------------------")
    println(bodyString)

    val ws = wsClient
      .url(s"https://davar.icfpcontest.org/teams/$teamId/solutions")
      .setAuth(":" + apiToken)
      .setContentType("application/json")
      .setBody(json)

    val response = ws.post(json).get(30, SECONDS)

    response.getStatus match {
      case 201 =>
        println("-------------------------------------------------------------------")
        println(s"Submission success for ${output.size} outputs, with tag: ${output.headOption.flatMap(_.tag).getOrElse("")}")
        println("-------------------------------------------------------------------")
        Success(Unit)
      case _ =>
        val error = response.getStatus + " (" + response.getStatusText + "): " + response.getBody
        println(error)
        Failure(new Exception(error))
    }
  }

  def outputOfProblemForStrategy(strategy: Strategy, tag: Option[String], problem: Int): Seq[Output] = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val problemFiles = ParseSpec.problems

    val gameExecutions = GameExecution.loadFile(strategy, problemFiles(problem), tag, None, None, phrasesOfPower)

    val futures = Future.sequence(gameExecutions.map(_.run))

    val results = Await.ready(futures, Int.MaxValue seconds).value.get

    val allOutputs = (results match {
      case Success(t) => t
      case Failure(e) => throw e
    })

    allOutputs.foreach(println(_))

    allOutputs
  }

  def outputsOfAllProblemsForStrategy(strategy: Strategy, tag: Option[String]): Seq[Output] = {

    import scala.concurrent.ExecutionContext.Implicits.global

    val problemFiles = ParseSpec.problems

    val gameExecutions = problemFiles.flatMap {
      GameExecution.loadFile(strategy, _, tag, None, None, phrasesOfPower)
    }

    val futures = Future.sequence(gameExecutions.map(_.run))

    val results = Await.ready(futures, Int.MaxValue seconds).value.get

    val allOutputs = (results match {
      case Success(t) => t
      case Failure(e) => throw e
    })

    allOutputs.foreach(println(_))

    allOutputs
  }
}
