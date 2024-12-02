package aoc2023

import scala.annotation.tailrec

object Day18 extends App {

  private val linesE = loadData("d18e.txt").get
  private val lines = loadData("d18.txt").get

  private def extractData(lines: List[String]): List[(String, Int, String)] = {
    lines.map(_.split(" ")).map(l => (l(0), l(1).toInt, l(2).substring(1, l(2).length - 1)))
  }

  private def digTrenches(directions: List[(String, Int, String)]): List[(Int, Int, String)] = {
    val dirs: Map[String, (Int, Int)] = Map("R" -> (0,1), "L" -> (0,-1), "U" -> (1,0), "D" -> (-1,0))

    @tailrec def digSide(dir: String, limit: Int, acc: List[(Int, Int, String)]): List[(Int, Int, String)] = {
      if (acc.size == limit) acc
      else {
        val dirP = dirs(dir)
        val newPoint = (acc.head._1 + dirP._1, acc.head._2 + dirP._2, acc.head._3)
        digSide(dir, limit, newPoint +: acc)
      }
    }

    @tailrec def digSides(toProcess: List[(String, Int, String)], at: (Int, Int), acc: List[(Int, Int, String)] ): List[(Int, Int, String)] ={
      if (toProcess.isEmpty) acc
      else {
        val dirP = dirs(toProcess.head._1)
        val curSide = digSide(toProcess.head._1, toProcess.head._2, List((at._1 + dirP._1, at._2 + dirP._2, toProcess.head._3)))
        digSides(toProcess.tail, (curSide.head._1, curSide.head._2), acc ++ curSide)
      }
    }

    digSides(directions, (0,0), List.empty)
  }

  private def digLagoon(lines: List[String]): Int = {
    val extracted = extractData(lines)
    val borders = digTrenches(extracted)
    val maxR = borders.map(_._1).max
    val minR = borders.map(_._1).min
    val maxC = borders.map(_._2).max
    val minC = borders.map(_._2).min
//    (maxR - 1 until minR).foreach(n => {
//      if (sides.count(_._1 == n) > 2) println(sides.filter(_._1 == n))
//    })

    def addInterior(row: Int): Int = {
      val sides = borders.filter(_._1 == row)
      sides.map(_._2).max - sides.map(_._2).min - sides.size + 1
    }

    borders.size + (minR + 1 until maxR).map(n => addInterior(n)).sum
  }

//  println(digTrenches(extractData(linesE)))
  println(digLagoon(linesE))
  println(digLagoon(lines)) //54688 too high

}
