package com.razorfish.icfp_2015.models

// see http://www.redblobgames.com/grids/hexagons/
private case class CubeCoord(x: Int, y: Int, z: Int) {
  def cell: Cell = {
    val col = x + (z - (z & 1)) / 2
    val row = z
    Cell(col, row)
  }

  def rotate(r: Rotation) = r match {
    case CW => CubeCoord(-z, -x, -y)
    case CCW => CubeCoord(-y, -z, -x)
  }

  def -(cc: CubeCoord): CubeCoord = {
    CubeCoord(x - cc.x, y - cc.y, z - cc.z)
  }

  def +(cc: CubeCoord): CubeCoord = {
    CubeCoord(x + cc.x, y + cc.y, z + cc.z)
  }
}

case class Cell(column: Int, row: Int) {

  def translate(m: Translation): Cell = {
    m match {
      case W => this.copy(column = column - 1)
      case E => this.copy(column = column + 1)
      case SW => {
        if (row % 2 == 0) Cell(column = column - 1, row = row + 1)
        else Cell(column, row = row + 1)
      }
      case SE => {
        if (row % 2 == 0) Cell(column = column + 1, row = row + 1)
        else Cell(column, row = row + 1)
      }
    }
  }

  private[models] def cubeCoords: CubeCoord = {
    val x = column - (row - (row & 1)) / 2
    val z = row
    val y = -x - z
    CubeCoord(x, y, z)
  }

  def rotate(r: Rotation, pivot: Cell): Cell = {
    val pivotCC = pivot.cubeCoords
    // cubic coordinate vector from pivot to this cell
    val vect = this.cubeCoords - pivotCC
    val newCubeCoords = pivotCC + vect.rotate(r)
    newCubeCoords.cell
  }

  def move(move: GameMove, pivot: Cell): Cell = {
    move match {
      case t: Translation => translate(t)
      case r: Rotation => rotate(r, pivot)
    }
  }

}

object NilCell extends Cell(0, 0)