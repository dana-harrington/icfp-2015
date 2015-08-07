package com.razorfish.icfp_2015.models

case class Cell(column: Int, row: Int) {

  def translate(m: Translation): Cell = {
    m match {
      case W => this.copy(column = column-1)
      case E => this.copy(column = column+1)
      case SW => Cell(column = column-1, row = row+1)
      case SE => Cell(column = column-1, row = row+1)
    }
  }

  def rotate(r: Rotation, pivot: Cell): Cell = {
    ???
  }

  def move(move: GameMove, pivot: Cell): Cell = {
    move match {
      case t: Translation => translate(t)
      case r: Rotation => rotate(r, pivot)
    }
  }
}
