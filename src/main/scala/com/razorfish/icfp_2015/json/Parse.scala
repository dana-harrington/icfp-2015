package com.razorfish.icfp_2015.json

import java.io.File
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import com.razorfish.icfp_2015.models.{Cell, GameUnit, Board}


case class Input(
                  /* A unique number identifying the problem */
                  id: Int,
                  /* The various unit configurations that may appear in this game.
     There might be multiple entries for the same unit.
     When a unit is spawned, it will start off in the orientation
     specified in this field. */
                  units: Seq[GameUnit],
                  /* The number of cells in a row */
                  width: Int,
                  /* The number of rows on the board */
                  height: Int,
                  /* Which cells start filled */
                  filled: Set[Cell],
                  /* How many units in the source */
                  sourceLength: Int,
                  /* How to generate the source and
                                 how many games to play */
                  sourceSeeds: Seq[Int]
                  )

object Input {
  // Our Cell has properties 'row' and 'column' to avoid collision with x,y,z of cubic coordinates
  private implicit val readsCell: Reads[Cell] = {
    ((__ \ "x").read[Int] and
      (__ \ "y").read[Int]
      )(Cell.apply _)
  }
  private implicit val readsGameUnit: Reads[GameUnit] = {
    ((__ \ "members").read[Set[Cell]] and
      (__ \ "pivot").read[Cell]
      )(GameUnit.apply _)
  }

  implicit val reads = Json.reads[Input]
}

case class Parse(file: File) {

  private[json] val input: Input = {
    val contents = scala.io.Source.fromFile(file).getLines().mkString("")
    val json = Json.parse(contents)
    json.as[Input]
  }

  val problemId = input.id

  val sourceSeeds = input.sourceSeeds

  val sourceLength = input.sourceLength

  val board: Board = Board(input.width, input.height, input.filled)

  val gameUnits: IndexedSeq[GameUnit] = {
    input.units.toArray[GameUnit]
  }
}