package aoc2021

import scala.util.Try

object Warmup extends App {

  val numerics = loadData("warmup.txt").get.map(_.toInt)

  def findTwoThatSum(list: List[Int], target: Int): (Int, Int) = {
    list.filter(e => list.contains(target - e))
      .map(el => (el, target - el)).head
  }

  def findThreeThatSum(list: List[Int], target: Int): (Int, Int, Int) = {
    list.map(e =>
      Try(findTwoThatSum(list, target - e))
        .map(a => (a._1, a._2, e)))
      .filter(t => t.isSuccess).head.get
  }

  println(findTwoThatSum(numerics, 2020))
  println(findThreeThatSum(numerics, 2020))

}
