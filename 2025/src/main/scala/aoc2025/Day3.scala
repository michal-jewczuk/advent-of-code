package aoc2025

import scala.annotation.tailrec

object Day3 extends App {
  private val linesE = loadData("d03e.txt").get
  private val lines = loadData("d03.txt").get

  private def getHighest(line: String): (Int, Int) = {
    @tailrec
    def getUntil(pos: Int, cur: Int, mx: Int): (Int, Int) = {
      if (pos == line.length) (cur, mx)
      else if (line.charAt(pos) == '9') (pos, 9)
      else {
        val nm = line.charAt(pos).toString.toInt
        if (nm > mx) getUntil(pos + 1, pos, nm)
        else getUntil(pos + 1, cur, mx)
      }
    }
    getUntil(0, 0, 0)
  }

  private def getJoltage(line: String): Int = {
    val first = getHighest(line.substring(0, line.length - 1))
    val second = getHighest(line.substring(first._1 + 1))
    first._2 * 10 + second._2
  }

  private def getJoltage12(line: String): Long = {
    var idx = 1
    val n1 = getHighest(line.substring(0, line.length - 11))
    idx += n1._1
    val n2 = getHighest(line.substring(idx, line.length - 10))
    idx += n2._1 + 1
    val n3 = getHighest(line.substring(idx, line.length - 9))
    idx += n3._1 + 1
    val n4 = getHighest(line.substring(idx, line.length - 8))
    idx += n4._1 + 1
    val n5 = getHighest(line.substring(idx, line.length - 7))
    idx += n5._1 +1
    val n6 = getHighest(line.substring(idx, line.length - 6))
    idx += n6._1 + 1
    val n7 = getHighest(line.substring(idx, line.length - 5))
    idx += n7._1 + 1
    val n8 = getHighest(line.substring(idx, line.length - 4))
    idx += n8._1 + 1
    val n9 = getHighest(line.substring(idx, line.length - 3))
    idx += n9._1 + 1
    val n10 = getHighest(line.substring(idx, line.length - 2))
    idx += n10._1 + 1
    val n11 = getHighest(line.substring(idx, line.length - 1))
    idx += n11._1 + 1
    val n12 = getHighest(line.substring(idx, line.length))
    ("" + n1._2 + n2._2 + n3._2 + n4._2 + n5._2 + n6._2 + n7._2 + n8._2 + n9._2 + n10._2 + n11._2 + n12._2).toLong
  }

  private def getSum(lines: List[String]): Int = {
    lines.map(getJoltage).sum
  }

  private def getSum12(lines: List[String]): Long = {
    lines.map(getJoltage12).sum
  }

  println(getSum(linesE))
  println(getSum(lines))
  println("===============")
  println(getSum12(linesE))
  println(getSum12(lines))
}
