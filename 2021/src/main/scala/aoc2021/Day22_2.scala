package aoc2021

import scala.annotation.tailrec
import scala.collection.mutable

object Day22_2 extends App {

  val linesE1 = loadData("d22e1.txt").get
  val linesE = loadData("d22e3.txt").get
  val lines = loadData("d22.txt").get

  val inputsE1 = linesE1.map(process)
  val inputsE = linesE.map(process)
  val inputs = lines.map(process)

  def process(line: String): (String, (Int, Int), (Int, Int), (Int, Int)) = {
    val state = line.substring(0, 3).trim
    val elements = line.substring(3).trim.split(",")
      .map(_.substring(2))
      .map(_.split("\\.\\.").toList)
      .map(l => (l.head.toInt, l.tail.head.toInt))

    (state, elements(0), elements(1), elements(2))
  }

  def generateCubesCount(first: (Int, Int), second: (Int, Int), third: (Int, Int)): Long = {
    1l * (first._1 to first._2).size * (second._1 to second._2).size * (third._1 to third._2).size
  }

  def findCommonArea(original: ((Int, Int), (Int, Int), (Int, Int)), ext: ((Int, Int), (Int, Int), (Int, Int))): ((Int, Int), (Int, Int), (Int, Int)) = {
    val (x1, y1, z1) = original
    val (x2, y2, z2) = ext

    if (x2._2 < x1._1 || x2._1 > x1._2) return ((-1, -2), (-1, -2), (-1, -2))
    if (y2._2 < y1._1 || y2._1 > y1._2) return ((-1, -2), (-1, -2), (-1, -2))
    if (z2._2 < z1._1 || z2._1 > z1._2) return ((-1, -2), (-1, -2), (-1, -2))

    ((Math.max(x1._1, x2._1), Math.min(x1._2, x2._2)), (Math.max(y1._1, y2._1), Math.min(y1._2, y2._2)), (Math.max(z1._1, z2._1), Math.min(z1._2, z2._2)))
  }


}
