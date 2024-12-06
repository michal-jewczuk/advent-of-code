package aoc2024

import scala.annotation.tailrec

object Day01 extends App {
  private val linesE = loadData("d01e.txt").get
  private val lines = loadData("d01.txt").get

  private def extractLists(lines: List[String]): (List[Int], List[Int]) = {
    @tailrec
    def extractUntil(lines: List[String], list1: List[Int], list2: List[Int]): (List[Int], List[Int]) = {
      if (lines.isEmpty) (list1.sorted, list2.sorted)
      else {
        val elements = lines.head.split("   ")
        extractUntil(lines.tail, list1 :+ elements.head.toInt, list2 :+ elements.tail.head.toInt)
      }
    }
    extractUntil(lines, List.empty, List.empty)
  }

  private def calculateDistance(lists: (List[Int], List[Int])): Int = {
    @tailrec def calculateUntil(list1: List[Int], list2: List[Int], acc: List[Int]): Int = {
      if (list1.isEmpty) acc.sum
      else {
        val el = Math.abs(list1.head - list2.head)
        calculateUntil(list1.tail, list2.tail, acc :+ el)
      }
    }

    calculateUntil(lists._1, lists._2, List.empty)
  }

  private def calculateSimilarity(lists: (List[Int], List[Int])): Int = {
    @tailrec def calculateUntil(list1: List[Int], list2: List[Int], acc: List[Int]): Int = {
      if (list1.isEmpty) acc.sum
      else {
        val el = list2.count(_ == list1.head) * list1.head
        calculateUntil(list1.tail, list2, acc :+ el)
      }
    }

    calculateUntil(lists._1, lists._2, List.empty)
  }

  println(calculateDistance(extractLists(linesE)))
  println(calculateDistance(extractLists(lines)))
  println("===============")
  println(calculateSimilarity(extractLists(linesE)))
  println(calculateSimilarity(extractLists(lines)))
}
