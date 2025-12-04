package aoc2025

import scala.annotation.tailrec
import scala.util.Try

object Day4 extends App {
  private val linesE = loadStrings("d04e.txt").map(_.toArray).toArray
  private val lines = loadStrings("d04.txt").map(_.toArray).toArray

  private def getMovable(grid: Array[Array[Char]]): List[(Int,Int)] = {
    var result: List[(Int, Int)] = List.empty
    grid.indices.foreach(row => grid(row).indices.foreach(col => {
      if (hasSpace(grid, row: Int, col: Int)) result = result :+ (row,col)
    }))
    result
  }

  private def hasSpace(grid: Array[Array[Char]], row: Int, col: Int): Boolean = {
    if (grid(row)(col) != '@') return false
    List((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1))
      .filter(p => Try(grid(row + p._1)(col + p._2)).getOrElse('.') == '@')
      .map(p => (row + p._1, col + p._2)).size < 4
  }

  private def remove(grid: Array[Array[Char]]): Int = {
    @tailrec
    def removeUntil(toRemove: List[(Int,Int)], total: Int): Int = {
      if (toRemove.isEmpty) total
      else {
        toRemove.foreach((r,c) => grid(r)(c) = '.')
        removeUntil(getMovable(grid), total + toRemove.size)
      }
    }
    removeUntil(getMovable(grid), 0)
  }

  println(getMovable(linesE).size)
  println(getMovable(lines).size)
  println("===============")
  println(remove(linesE))
  println(remove(lines))
}
