package aoc2021

import scala.annotation.tailrec

object Day01_1 extends App {

  val depthsExample = loadData("d01e.txt").get.map(_.toInt)
  val depths = loadData("d01.txt").get.map(_.toInt)

  def countChanges(list: List[Int]): Int = {
    @tailrec
    def accumulator(l: List[Int], acc: Int): Int = {
      if (l.tail == Nil) acc
      else accumulator(l.tail, if (l.tail.head > l.head) acc + 1 else acc)
    }

    accumulator(list, 0)
  }

  println(countChanges(depthsExample))
  println(countChanges(depths))

}
