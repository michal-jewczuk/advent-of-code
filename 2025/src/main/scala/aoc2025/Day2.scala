package aoc2025

import scala.annotation.tailrec

object Day2 extends App {
  private val linesE = loadData("d02e.txt").get.head
  private val lines = loadData("d02.txt").get.head

  private def extractData(line: String): List[(Long, Long)] = {
    line.split(",").toList.map(_.split("-")).map(p => (p(0).toLong, p(1).toLong))
  }

  private def isInvalid(number: Long, simple: Boolean): Boolean = {
    val str = number.toString;
    val ln = str.length;

    if (simple) {
      if (ln % 2 != 0) return false
      str.substring(0, ln / 2).equals(str.substring(ln / 2))
    } else {
      if (ln == 1) return false // apparently possible :)
      if (ln % 2 == 0 && str.substring(0, ln / 2).equals(str.substring(ln / 2))) return true
      if (ln == 9 && List(str.substring(0,3),
        str.substring(3,6), str.substring(6)).toSet.size == 1) return true
      if (ln == 6 && List(str.substring(0,2),
        str.substring(2,4), str.substring(4)).toSet.size == 1) return true
      if (ln == 10 && List(str.substring(0, 2), str.substring(2, 4),
        str.substring(4, 6), str.substring(6, 8), str.substring(8)).toSet.size == 1) return true

      str.toSet.size == 1
    }
  }

  private def getInvalids(from: Long, to: Long, simple: Boolean): List[Long] = {
    @tailrec
    def getUntil(current: Long, invalids: List[Long]): List[Long] = {
      if (current > to) invalids
      else {
        val inv = if isInvalid(current, simple) then invalids :+ current else invalids
        getUntil(current + 1, inv)
      }
    }

    getUntil(from, List.empty)
  }

  private def checkInvalids(ranges: List[(Long, Long)], simple: Boolean): Set[Long] = {
    @tailrec
    def checkUntil(rest: List[(Long, Long)], invalids: Set[Long]): Set[Long] = {
      if (rest.isEmpty) invalids
      else {
        val inv = getInvalids(rest.head._1, rest.head._2, simple)
        checkUntil(rest.tail, invalids ++ inv)
      }
    }

    checkUntil(ranges, Set.empty)
  }

  private def calculate(line: String, simple: Boolean): Long = {
    checkInvalids(extractData(line), simple).sum
  }

  println(calculate(linesE, true))
  println(calculate(lines, true))
  println("===============")
  println(calculate(linesE, false))
  println(calculate(lines, false))
}
