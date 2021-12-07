package aoc2021

import scala.annotation.tailrec
import scala.collection.mutable

object D07_2 extends App {

  val positionsE = loadData("d07e.txt").get.head.split(",").map(_.toInt).toList
  val positions = loadData("d07.txt").get.head.split(",").map(_.toInt).toList

  def countFuelNeededForPosition(pos: List[Int], target: Int): Int = {
    @tailrec
    def sumUpTo(limit: Int, current: Int, acc: Int): Int = {
      if (current == limit) acc + current
      else sumUpTo(limit, current + 1, acc + current)
    }

    pos.map(p => sumUpTo(Math.abs(p - target), 0, 0)).sum
  }

  def determineCostsForAllPositions(pos: List[Int]): mutable.Map[Int, Int] = {
    val costs = mutable.Map[Int, Int]()
    (0 to pos.max).foreach(n => costs(n) = countFuelNeededForPosition(pos, n))
    costs
  }

  println(determineCostsForAllPositions(positionsE))
  println(determineCostsForAllPositions(positions).values.min)
}
