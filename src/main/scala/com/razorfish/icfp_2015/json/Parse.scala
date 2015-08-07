package com.razorfish.icfp_2015.json

import java.io.File
import play.api.libs.json._

import com.razorfish.icfp_2015.models.Board

case class Cell(x: Int, y: Int)

object Cell {
  implicit val format = Json.format[Cell]
}

case class Unit(members: Seq[Cell], pivot: Cell)

object Unit {
  implicit val format = Json.format[Unit]
}

case class Input(
                  /* A unique number identifying the problem */
                  id: Long,
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

object Parse {

  def fromFile(file: File): Input = {
    val contents = scala.io.Source.fromFile(file).getLines().mkString("")
    val json = Json.parse(contents)

    val input = json.as[Input]
    input

  }

  def createBoard(file: File): Board = {
    val input = fromFile(file)


    val filledCells = input.filled.map {
      case Cell(x, y) => com.razorfish.icfp_2015.models.Cell(x, y)
    }.toSet

    new Board(input.width, input.height, filledCells)
  }
}