package aoc2021

import scala.annotation.tailrec
import scala.collection.mutable

object Day15_1 extends App {

  val caveE = convertToArray(loadData("d15e.txt").get)
  val cave = convertToArray(loadData("d15.txt").get)

  def moveOneStep(entries: Map[(Int, Int), Int], cave: Array[Array[Int]]): Map[(Int, Int), Int] = {
    val result: mutable.Map[(Int, Int), Int] = mutable.Map()
    entries.keys.foreach(key => {
      val newPoint1 = (key._1 + 1, key._2)
      val newPoint2 = (key._1, key._2 + 1)
      if (newPoint1._1 < cave.length) {
        result(newPoint1) = Math.min(entries(key) + cave(newPoint1._1)(newPoint1._2), result.getOrElse(newPoint1, Int.MaxValue))
      }
      if (newPoint2._2 < cave.length) {
        result(newPoint2) = Math.min(entries(key) + cave(newPoint2._1)(newPoint2._2), result.getOrElse(newPoint2, Int.MaxValue))
      }
    })
    result.toMap
  }

  def findLowestRiskPath(cave: Array[Array[Int]]): Int = {
    @tailrec
    def moveUntil(entries: Map[(Int, Int), Int]): Map[(Int, Int), Int] = {
      if (entries.keys.size == 1 && entries.keys.head != (0,0)) entries
      else moveUntil(moveOneStep(entries, cave))
    }

    moveUntil(Map((0,0) -> 0)).values.head
  }

  println(findLowestRiskPath(caveE))
  println(findLowestRiskPath(cave))

}
