package aoc2021

import scala.collection.mutable

object D07_1 extends App {

  val positionsE = loadData("d07e.txt").get.head.split(",").map(_.toInt).toList
  val positions = loadData("d07.txt").get.head.split(",").map(_.toInt).toList

  def countFuelNeededForPosition(pos: List[Int], target: Int): Int = {
    pos.map(p => Math.abs(p - target)).sum
  }

  def determineCostsForAllPositions(pos: List[Int]): mutable.Map[Int, Int] = {
    val costs = mutable.Map[Int, Int]()
    (0 to pos.max).foreach(n => costs(n) = countFuelNeededForPosition(pos, n))
    costs
  }
  
  println(determineCostsForAllPositions(positionsE).values.min)
  println(determineCostsForAllPositions(positions).values.min)
}
