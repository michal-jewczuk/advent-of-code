package aoc2021

import scala.annotation.tailrec

object Day01_2 extends App {

  val depthsExample = loadData("d011e.txt").get.map(_.toInt)
  val depths = loadData("d011.txt").get.map(_.toInt)

  def countChanges(list: List[Int]): Int = {
    @tailrec
    def accumulator(l: List[Int], acc: Int): Int = {
      if (l.tail == Nil) acc
      else accumulator(l.tail, if (l.tail.head > l.head) acc + 1 else acc)
    }

    accumulator(list, 0)
  }

  def calculateWindows(list: List[Int]): List[Int] = {
    @tailrec
    def accumulator(l: List[Int], acc: List[Int]): List[Int] = {
      if (l.tail.tail == Nil) acc
      else accumulator(l.tail, acc :+ (l.head + l.tail.head + l.tail.tail.head))
    }

    accumulator(list, Nil)
  }

  println(countChanges(calculateWindows(depthsExample)))
  println(countChanges(calculateWindows(depths)))
}
