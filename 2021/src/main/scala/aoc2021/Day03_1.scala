package aoc2021

import scala.annotation.tailrec

object Day03_1 extends App {

  val reportExample = loadData("d03e.txt").get
  val report = loadData("d03.txt").get

  def countNumberOfZerosAtPosition(lines: List[String], pos: Int): Int = {
    lines.count(line => line.charAt(pos).equals('0'))
  }

  def calculateGammaAndEpsilon(lines: List[String]): (String, String) = {
    @tailrec
    def accumulate(accumulators: (String, String), pos: Int): (String, String) = {
      if (pos >= lines.head.length) accumulators
      else accumulate(addByte(accumulators, pos), pos + 1)
    }

    def addByte(gamEps: (String, String), p: Int): (String, String) = {
      if (countNumberOfZerosAtPosition(lines, p) * 2 > lines.size) (gamEps._1 + "0", gamEps._2 + "1")
      else (gamEps._1 + "1", gamEps._2 + "0")
    }

    accumulate(("", ""),0)
  }

  val reportExampleResult = calculateGammaAndEpsilon(reportExample)
  val reportResult = calculateGammaAndEpsilon(report)

  println(reportExampleResult)
  println(binaryToInt(reportExampleResult._1) * binaryToInt(reportExampleResult._2))
  println(reportResult)
  println(binaryToInt(reportResult._1) * binaryToInt(reportResult._2))
}
