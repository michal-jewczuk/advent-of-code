package aoc2021

import scala.annotation.tailrec
import scala.util.Try

object Day09_2 extends App {

  val heightmapE = convertToArray(loadData("d09e.txt").get)
  val heightmap = convertToArray(loadData("d09.txt").get)

  def isLowestPoint(all: Array[Array[Int]], row: Int, col: Int): Boolean = {
    def isOffsetHigher(point: (Int, Int)): Boolean = {
      Try(all(row + point._1)(col + point._2))
        .map(_ > all(row)(col))
        .getOrElse(true)
    }

    Set((1,0), (-1,0), (0,1), (0,-1)).map(isOffsetHigher).count(_ == false) == 0
  }

  def findAllLowestPoints(all: Array[Array[Int]]): List[(Int, Int)] = {
    all.indices.flatMap(row =>
      all(row).indices.filter(col => isLowestPoint(all, row, col)).map((row, _))
    ).toList
  }

  def findBasinSize(all: Array[Array[Int]], lowest: (Int, Int)): Int = {
    def getHigherPoints(cur: (Int, Int)): Set[(Int, Int)] = {
      Set((1,0), (-1,0), (0,1), (0,-1)).filter(point => {
        Try(all(cur._1 + point._1)(cur._2 + point._2))
          .map(p => p != 9 && p > all(cur._1)(cur._2))
          .getOrElse(false)
      }).map(p => (p._1 + cur._1, p._2 + cur._2))
    }

    @tailrec
    def bfs(basin: Set[(Int, Int)], checked: Set[(Int, Int)]): Set[(Int, Int)] = {
      if (checked.isEmpty) basin
      else {
        val higher = getHigherPoints(checked.head)
        bfs(basin + checked.head, checked.tail ++ higher)
      }
    }

    bfs(Set(), Set(lowest)).size
  }

  def findProductOfThreeHighest(all: Array[Array[Int]]) = {
    findAllLowestPoints(all).map(findBasinSize(all, _)).sorted.reverse.slice(0, 3).product
  }

  println(findProductOfThreeHighest(heightmapE))
  println(findProductOfThreeHighest(heightmap))
}
