package aoc2021

import scala.annotation.tailrec

object Day03_2 extends App {

  val reportExample = loadData("d03e.txt").get
  val report = loadData("d03.txt").get

  def countNumberOfZerosAtPosition(lines: List[String], pos: Int): Int = {
    lines.count(line => line.charAt(pos).equals('0'))
  }

  def countRatings(lines: List[String], oxygen: Boolean): String = {
    @tailrec
    def filterUntilOnlyOne(elements: List[String], pos: Int): String = {
      if (elements.tail == Nil) elements.head
      else filterUntilOnlyOne(removeNotInPattern(elements, pos, oxygen), pos + 1)
    }

    def removeNotInPattern(all: List[String], pos: Int, isOxygen: Boolean): List[String] = {
      val zerosCount = countNumberOfZerosAtPosition(all, pos)
      val oxygenChar = if zerosCount * 2 > all.length then '0' else '1'
      val co2Char = if zerosCount * 2 > all.length then '1' else '0'
      val charToFilterBy = if isOxygen then oxygenChar else co2Char
      all.filter(line => line.charAt(pos).equals(charToFilterBy))
    }

    filterUntilOnlyOne(lines, 0)
  }

  val oxygenExample = countRatings(reportExample, true)
  val co2Example = countRatings(reportExample, false)
  val oxygen = countRatings(report, true)
  val co2 = countRatings(report, false)

  println(binaryToInt(oxygenExample) * binaryToInt(co2Example))
  println(binaryToInt(oxygen) * binaryToInt(co2))

}
