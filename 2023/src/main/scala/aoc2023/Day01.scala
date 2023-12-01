package aoc2023

import scala.annotation.tailrec

object Day01 extends App {

  private val linesE = loadData("d01e.txt").get
  private val linesE2 = loadData("d01e2.txt").get
  val lines = loadData("d01.txt").get

  private val stringToDigit: Map[String, String] = Map(
    "one" -> "1", "two" -> "2", "three" -> "3",
    "four" -> "4", "five" -> "5", "six" -> "6",
    "seven" -> "7", "eight" -> "8", "nine" -> "9"
  )

  private def countSum(input: List[String]): Int = {
    input.map(l => getNumber(l)).sum
  }

  private def getNumber(line: String): Int = {
    (getFirstNumber(line, 1) + getFirstNumber(line, -1)).toInt
  }

  private def getFirstNumber(line: String, inc: Int): String = {
    val start = if(inc > 0) 0 else line.length - 1
    val end = if (inc > 0) line.length else -1
    var i = start
    while i != end do
      if (line.charAt(i).isDigit) return line.charAt(i).toString
      else i += inc
    ""
  }

  private def countSum2(input: List[String]): Int = {
    input.map(l => getNumber2(l)).sum
  }

  private def getNumber2(line: String): Int = {
    (getFirstNumber2(line, false) + getFirstNumber2(line.reverse, true)).toInt
  }

  private def getFirstNumber2(line: String, isRev: Boolean): String = {
    val digitKeys = if (isRev) stringToDigit.keySet.map(k => k.reverse) else stringToDigit.keySet
    def startsWithString(str: String, idx: Int): String = {
      @tailrec def getStartsWith(keys: Set[String]): String = {
        if (keys.isEmpty) ""
        else if (str.startsWith(keys.head, idx) && isRev) keys.head.reverse
        else if (str.startsWith(keys.head, idx) && !isRev) keys.head
        else getStartsWith(keys.tail)
      }
      getStartsWith(digitKeys)
    }

    var i = 0
    while i < line.length do
      if (line.charAt(i).isDigit) return line.charAt(i).toString
      else {
        val digitString = startsWithString(line, i)
        if (digitString != "") return stringToDigit(digitString)
        else i += 1
      }
    ""
  }

  println(countSum(linesE))
  println(countSum(lines)) //54667
  println("===============")
  println(countSum2(linesE2))
  println(countSum2(lines)) //54203
}
