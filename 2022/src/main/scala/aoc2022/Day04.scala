package aoc2022

object Day04 extends App {

  val linesE = loadStrings("d04e.txt")
  val lines = loadStrings("d04.txt")

  private def extractPairs(input: List[String]): List[((Int, Int), (Int, Int))] = {
    input
      .map(_.split(","))
      .map(e => (e(0).split("-"), e(1).split("-")))
      .map(a => ((a._1(0).toInt, a._1(1).toInt) , (a._2(0).toInt, a._2(1).toInt)))
  }

  private def isContained(p1: (Int, Int), p2: (Int, Int)): Boolean = {
    (p1._1 >= p2._1 && p1._2 <= p2._2) || (p2._1 >= p1._1 && p2._2 <= p1._2)
  }

  private def findContained(input: List[String]): Int = {
    extractPairs(input).count(p => isContained(p._1, p._2))
  }

  private def isOverlapping(p1: (Int, Int), p2: (Int, Int)): Boolean = {
    !(p1._2 < p2._1 || p1._1 > p2._2)
  }

  private def findOverlapping(input: List[String]): Int = {
    extractPairs(input).count(p => isOverlapping(p._1, p._2))
  }

  println(findContained(linesE))
  println(findContained(lines))
  println("================")
  println(findOverlapping(linesE))
  println(findOverlapping(lines))
}
