package aoc2022

import scala.annotation.tailrec

object Day15 extends App {

  val linesE = loadStrings("d15e.txt")
  val lines = loadStrings("d15.txt")

  private def processLines(input: List[String]): List[((Int, Int), (Int, Int))] = {
    def processSingleLine(line: String): ((Int, Int), (Int, Int)) = {
      val elems = line.split(": ")

      def extractXY(phrase: String): (Int, Int) = {
        val result = if (phrase.startsWith("Sensor")) phrase.substring(12) else phrase.substring(23)
        val a = result.split(", y=")
        (a(0).toString.toInt, a(1).toString.toInt)
      }

      (extractXY(elems(0)), extractXY(elems(1)))
    }

    input.map(processSingleLine)
  }

  private def filterAllRelevantBeacons(all: List[((Int, Int), (Int, Int))], targetY: Int): List[((Int, Int), (Int, Int))] = {
    def isRelevant(sensor: ((Int, Int), (Int, Int)), target: Int): Boolean = {
      val md = (sensor._1._1 - sensor._2._1).abs + (sensor._1._2 - sensor._2._2).abs
      (sensor._1._2 - target).abs <= md
    }

    all.filter(isRelevant(_, targetY))
  }

  private def findNotLocations(input: List[String], targetY: Int): List[(Int, Int)] = {
    val all = processLines(input)

    @tailrec
    def findUntil(sensors: List[((Int, Int), (Int, Int))], acc: Set[(Int, Int)]): List[(Int, Int)] = {
      if (sensors.isEmpty) acc.toList
      else findUntil(sensors.tail, acc ++ findForOne(sensors.head))
    }

    def findForOne(sensor: ((Int, Int), (Int, Int))): List[(Int, Int)] = {
      val md = (sensor._1._1 - sensor._2._1).abs + (sensor._1._2 - sensor._2._2).abs
      val startX = sensor._1._1 - md + (sensor._1._2 - targetY).abs
      val endX = sensor._1._1 + md - (sensor._1._2 - targetY).abs
      (startX to endX).map(i => (i, targetY)).toList
    }

    val beaconsOnTarget = all.map(_._2).filter(_._2 == targetY)
    findUntil(filterAllRelevantBeacons(all, targetY), Set.empty).filter(!beaconsOnTarget.contains(_))
  }

  private def findDistress(input: List[String], x: Int, y: Int): Long = {
    val all = processLines(input)
    val minX = 0
    val minY = 0

    @tailrec
    def findUntil(accX: Int, accY: Int): (Int, Int) = {
      val nX = isNotForAllSensors(all, (accX, accY))
      if (nX == accX) (accX, accY)
      else if (nX >= x) findUntil(minX, accY + 1)
      else findUntil(nX + 1, accY)
    }

    @tailrec
    def isNotForAllSensors(sensors: List[((Int, Int), (Int, Int))], pos: (Int, Int)): Int = {
      if (sensors.isEmpty) pos._1
      else {
        val nPos = isNotForOneSensor(pos, sensors.head)
        if (nPos != pos._1) nPos
        else isNotForAllSensors(sensors.tail, pos)
      }
    }

    def isNotForOneSensor(pos: (Int, Int), sensor: ((Int, Int), (Int, Int))): Int = {
      val md = (sensor._1._1 - sensor._2._1).abs + (sensor._1._2 - sensor._2._2).abs
      if ((sensor._1._1 - pos._1).abs + (sensor._1._2 - pos._2).abs <= md) sensor._1._1 + md - (sensor._1._2 - pos._2).abs
      else pos._1
    }

    val (xR, yR) = findUntil(minX, minY)
    println((xR, yR))
    (xR.toLong * 4000000) + yR
  }

  println(findNotLocations(linesE, 10).size)
  println(findNotLocations(lines, 2000000).size)
  println("================")
  println(findDistress(linesE, 20, 20))
  println(findDistress(lines, 4000000, 4000000))
}
