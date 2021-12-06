package aoc2021

import scala.collection.mutable

object Day06 extends App {

  val startingE = loadData("d06e.txt").get.head.split(",").map(_.toInt).toList
  val starting = loadData("d06.txt").get.head.split(",").map(_.toInt).toList

  def createMap(elements: List[Int]): mutable.Map[Int, Long] = {
    val all = mutable.Map[Int, Long]()
    (0 to 8).foreach(n => all(n) = elements.count(_ == n))
    all
  }

  def simulateOneDay(elements: mutable.Map[Int, Long]): mutable.Map[Int, Long] = {
    val all = mutable.Map.from(elements)
    (0 to 7).foreach(n =>
      if (n == 6) all(6) = elements(7) + elements(0)
      else all(n) = elements(n + 1)
    )
    all += 8 -> elements(0)
  }

  def simulateNDays(elements: mutable.Map[Int, Long], count: Int): mutable.Map[Int, Long] = {
    var all = elements
    (1 to count).foreach(n => all = simulateOneDay(all))
    all
  }

  val fishesE = createMap(startingE)
  val after80E = simulateNDays(fishesE, 80)
  println(after80E.values.sum)
  val after256E = simulateNDays(fishesE,256)
  println(after256E.values.sum)

  val fishes = createMap(starting)
  val after80 = simulateNDays(fishes, 80)
  println(after80.values.sum)
  val after256 = simulateNDays(fishes,256)
  println(after256.values.sum)
}
