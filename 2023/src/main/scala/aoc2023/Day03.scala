package aoc2023

import scala.util.Try

object Day03 extends App {

  private val linesE = loadStrings("d03e.txt").map(_.toArray).toArray
  private val lines = loadStrings("d03.txt").map(_.toArray).toArray

  private def extractNumbers(grid: Array[Array[Char]]): List[Int] = {
    var validNumbers: List[Int] = List.empty
    var curNum = ""
    var processingNumber = false
    var isBySymbol = false
    grid.indices.foreach(row => grid(row).indices.foreach(col => {
      if (grid(row)(col).isDigit) {
        processingNumber = true
        curNum += grid(row)(col)
        if (!isBySymbol) isBySymbol = hasSymbolNearby(row, col, grid)
      }
      else if(processingNumber) {
        if (isBySymbol) validNumbers = validNumbers :+ curNum.toInt
        processingNumber = false
        isBySymbol = false
        curNum = ""
      }
    }))
    validNumbers
  }

  private def hasSymbolNearby(row: Int, col: Int, grid: Array[Array[Char]]): Boolean = {
    List((-1,-1), (-1,0), (-1,1), (0,-1), (0,1), (1,-1), (1,0), (1,1))
      .map(p => Try(grid(row + p._1)(col + p._2))).filter(_.isSuccess)
      .map(_.get)
      .count(c => !c.isDigit && c != '.') > 0
  }

  private def extractNumbersAndGears(grid: Array[Array[Char]]): List[((Int, Int), Int)] = {
    var validNumbers: List[((Int, Int), Int)] = List.empty
    var gears: List[(Int, Int)] = List.empty
    var curNum = ""
    var processingNumber = false
    var isByGear = false
    grid.indices.foreach(row => grid(row).indices.foreach(col => {
      if (grid(row)(col).isDigit) {
        processingNumber = true
        curNum += grid(row)(col)
        if (gears.isEmpty) gears = hasGearNearby(row, col, grid)
      }
      else if (processingNumber) {
        if(gears.nonEmpty) gears.foreach(g => validNumbers = validNumbers :+ (g, curNum.toInt))
        processingNumber = false
        gears = List.empty
        curNum = ""
      }
    }))
    validNumbers
  }

  private def hasGearNearby(row: Int, col: Int, grid: Array[Array[Char]]): List[(Int, Int)] = {
    List((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1))
      .filter(p => Try(grid(row + p._1)(col + p._2)).getOrElse('-') == '*')
      .map(p => (row + p._1, col + p._2))
  }

  private def calculateRations(gears: List[((Int, Int), Int)]): Int = {
    val keys = gears.map(_._1).toSet.filter(k => gears.map(_._1).count(_ == k) == 2)
    keys.map(k => gears.filter(_._1 == k).map(_._2).product).sum
  }

  println(extractNumbers(linesE).sum)
  println(extractNumbers(lines).sum)
  println("===============")
  println(calculateRations(extractNumbersAndGears(linesE)))
  println(calculateRations(extractNumbersAndGears(lines)))
}
