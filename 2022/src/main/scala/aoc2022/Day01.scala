package aoc2022

import scala.annotation.tailrec
import scala.util.Try

object Day01 extends App {

  val linesE = loadData("d01e.txt").get
  val lines = loadData("d01.txt").get

  def sumCalories(input: List[String]): List[Int] = {
    @tailrec
    def findUntil(rest: List[String], acc: Int, result: List[Int]): List[Int] = {
      if (rest.isEmpty) result :+ acc
      else {
        val numTry = Try(rest.head.toInt)
        if (numTry.isSuccess) findUntil(rest.tail, acc + numTry.get, result)
        else findUntil(rest.tail, 0, result :+ acc)
      }
    }
    findUntil(input, 0, List.empty)
  }

  println(sumCalories(linesE).max)
  println(sumCalories(lines).max)
  println("================")
  println(sumCalories(linesE).sorted(Ordering.Int.reverse).slice(0, 3).sum)
  println(sumCalories(lines).sorted(Ordering.Int.reverse).slice(0, 3).sum)
}
