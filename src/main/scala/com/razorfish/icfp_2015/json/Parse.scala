package com.razorfish.icfp_2015.json

import java.io.File
import play.api.libs.json._

import com.razorfish.icfp_2015.models.{GameUnit, Board}

case class Cell(x: Int, y: Int){
  val toCell = com.razorfish.icfp_2015.models.Cell(x, y)
}

object Cell {
  implicit val format = Json.format[Cell]
}

case class Unit(members: Seq[Cell], pivot: Cell)

object Unit {
  implicit val format = Json.format[Unit]
}

case class Input(
                  /* A unique number identifying the problem */
                  id: Int,
                  /* The various unit configurations that may appear in this game.
     There might be multiple entries for the same unit.
     When a unit is spawned, it will start off in the orientation
     specified in this field. */
                  units: Seq[Unit],
                  /* The number of cells in a row */
                  width: Int,
                  /* The number of rows on the board */
                  height: Int,
                  /* Which cells start filled */
                  filled: Seq[Cell],
                  /* How many units in the source */
                  sourceLength: Int,
                  /* How to generate the source and
                                 how many games to play */
                  sourceSeeds: Seq[Int]
                  )

object Input {
  implicit val format = Json.format[Input]
}

case class Parse(file: File) {

  private[json] val input: Input = {
    val contents = scala.io.Source.fromFile(file).getLines().mkString("")
    val json = Json.parse(contents)
    json.as[Input]
  }

  val problemId = input.id

  val sourceSeeds = input.sourceSeeds

  val board: Board = {

    val filledCells = input.filled.map(_.toCell).toSet

    new Board(input.width, input.height, filledCells)
  }

  val gameUnits: Set[GameUnit] = {
    input.units.map {
      case Unit(members: Seq[Cell], pivot: Cell) => {
        GameUnit(members.map(_.toCell).toSet, pivot.toCell)
      }
    }.toSet
  }
}