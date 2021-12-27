package aoc2021

import scala.annotation.tailrec
import scala.collection.mutable

object Day25 extends App {

  val herdsE = convertToArrayStrings(loadData("d25e.txt").get)
  val herds = convertToArrayStrings(loadData("d25.txt").get)

  def findLastMove(start: Array[Array[String]]): Int = {

    def findMovable(from: Array[Array[String]], east: Boolean): Set[(Int, Int)] = {
      val movable: mutable.Set[(Int,Int)] = mutable.Set()
      from.indices.foreach(row => from(0).indices.foreach(col => {
        if (east) {
          if (from(row)(col).equals(">") && from(row)((col + 1) % from(0).length).equals(".")) movable.addOne((row,col))
        } else {
          if (from(row)(col).equals("v") && from((row + 1) % from.length)(col).equals(".")) movable.addOne((row,col))
        }
      }))
      movable.toSet
    }

    def applyMove(from: Array[Array[String]], values: Set[(Int, Int)], east: Boolean): Array[Array[String]] = {
      values.foreach(v => {
        from(v._1)(v._2) = "."
        if (east) from(v._1)((v._2 + 1) % from(0).length) = ">"
        else from((v._1 + 1) % from.length)(v._2) = "v"
      })
      from
    }

    def move(from: Array[Array[String]]): (Array[Array[String]], Boolean) = {
      val movableEast = findMovable(from, true)
      val afterEastMoved = applyMove(from, movableEast, true)
      val movableSouth = findMovable(afterEastMoved, false)
      (applyMove(afterEastMoved, movableSouth, false), movableEast.isEmpty && movableSouth.isEmpty)
    }

    @tailrec
    def moveUntil(current: Array[Array[String]], hasNotMoved: Boolean, count: Int): Int = {
      if (hasNotMoved) count
      else {
        val (afterMove, hasNotMoved) = move(current)
        moveUntil(afterMove, hasNotMoved, count + 1)
      }
    }

    moveUntil(start, false, 0)
  }

  println(findLastMove(herdsE))
  println(findLastMove(herds))
}
