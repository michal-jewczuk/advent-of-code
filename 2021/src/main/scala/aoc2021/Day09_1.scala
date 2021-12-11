package aoc2021

import scala.util.Try

object Day09_1 extends App {

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

  def countRiskLevels(all: Array[Array[Int]]): Int = {
    all.indices.flatMap(row =>
      all(row).indices.filter(col => isLowestPoint(all, row, col)).map(all(row)(_))
    ).toList.map(_ + 1).sum
  }

  println(countRiskLevels(heightmapE))
  println(countRiskLevels(heightmap))

}
