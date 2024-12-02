package aoc2023

import scala.annotation.tailrec

object Day13 extends App {

  private val linesE = loadStrings("d13e.txt")
  private val lines = loadStrings("d13.txt")

  private val patternsE = extractPatterns(linesE)
  private val patterns = extractPatterns(lines)

  private def extractPatterns(lines: List[String]): List[Array[Array[Char]]] = {
    @tailrec def extractAcc(toProcess: List[String], cur: List[String], acc: List[Array[Array[Char]]]): List[Array[Array[Char]]] = {
      if (toProcess.isEmpty) acc :+ cur.map(_.toArray).toArray
      else {
        if (toProcess.head.isBlank) extractAcc(toProcess.tail, List.empty, acc :+ cur.map(_.toArray).toArray)
        else extractAcc(toProcess.tail, cur :+ toProcess.head, acc)
      }
    }

    extractAcc(lines, List.empty, List.empty)
  }

  private def findVertical(pattern: Array[Array[Char]]): Int = {
    val cols = pattern.head.length

    def colToArray(colIdx: Int): Array[Char] = {
      pattern.indices.map(pattern(_)(colIdx)).toArray
    }

    @tailrec def findMatchingCol(cur: Array[Char], idx: Int): Int = {
      if (idx == cols) -1
      else {
        if (cur sameElements colToArray(idx)) idx
        else findMatchingCol(cur, idx + 1)
      }
    }
    val result = pattern.head.indices.map(i => (i, findMatchingCol(colToArray(i), i + 1))).filter(_._2 != -1)
    val tmp = result.filter(p => p._2 - p._1 == 1)
    if (tmp.size > 1) {
      println(tmp)
      println(tmp.map(e => isValid(e._2, cols, result)))
    }
//    val col = if (tmp.size != 1) cols else tmp.head._2

    val res = tmp.map(e => isValid(e._2, cols, result))
    if (res.count(_ == -1) == res.size) - 1
    else res.filter(_ != -1).head
  }

  private def findHorizontal(pattern: Array[Array[Char]]): Int = {
    val rows = pattern.length

    @tailrec def findMatchingRow(cur: Array[Char], idx: Int): Int = {
      if (idx == rows) -1
      else {
        if (cur sameElements pattern(idx)) idx
        else findMatchingRow(cur, idx + 1)
      }
    }
    val result = pattern.indices.map(i => (i, findMatchingRow(pattern(i), i + 1))).filter(_._2 != -1)
    val tmp = result.filter(p => p._2 - p._1 == 1)
    if (tmp.size > 1) {
      println(tmp)
      println(tmp.map(e => isValid(e._2, rows, result)))
    }
    val row = if (tmp.size != 1) rows else tmp.head._2

//    isValid(row, rows, result)
    val res = tmp.map(e => isValid(e._2, rows, result))
    if (res.count(_ == -1) == res.size) -1
    else res.filter(_ != -1).head
  }

  private def isValid(idx: Int, total: Int, values: Seq[(Int, Int)]): Int = {
    if (idx >= total / 2) {
      if ((idx until total).toList.reverse.equals(values.map(_._2).toList)) idx
      else -1
    } else {
      if ((0 until idx).toList.reverse.equals(values.map(_._1).toList)) idx
      else -1
    }
  }

  private def calculateResult(elements: List[Array[Array[Char]]]): Int = {
    @tailrec def calculateAcc(toProcess: List[Array[Array[Char]]], acc: Int): Int = {
      if (toProcess.isEmpty) acc
      else {
        val vert = findVertical(toProcess.head)
        val hor = findHorizontal(toProcess.head)
        val toAdd = if (hor != -1) hor * 100 else vert
        calculateAcc(toProcess.tail, acc + toAdd)
      }
    }

    calculateAcc(elements, 0)
  }

  private def findHorizontal2(pattern: Array[Array[Char]]): Int = {
    val rows = pattern.length

    @tailrec def findAcc(idx: Int, toCheck: Int): Int = {
      if (idx == rows) -1
      else {
        if (toCheck > idx) {
          if (pattern(idx) sameElements pattern(toCheck)) {
            val half = toCheck - idx
            if (half % 2 == 0) {
              if ((idx to half).count(n => pattern(n) sameElements pattern(toCheck - n)) == half - idx) half
              else findAcc(idx, toCheck - 1)
            }
            else findAcc(idx, toCheck - 1)
          }
          else findAcc(idx, toCheck - 1)
        }
        else findAcc(idx + 1, rows - 1)
      }
    }

    findAcc(0, rows - 1)
  }

  private def findVertical2(pattern: Array[Array[Char]]): Int = {
    val rows = pattern.head.length

    def colToArray(colIdx: Int): Array[Char] = {
      pattern.indices.map(pattern(_)(colIdx)).toArray
    }

    @tailrec def findAcc(idx: Int, toCheck: Int): Int = {
      if (idx == rows) -1
      else {
        if (toCheck > idx) {
          if (colToArray(idx) sameElements colToArray(toCheck)) {
            val half = toCheck - idx
            if (half % 2 == 0) {
              if ((idx to half).count(n => colToArray(n) sameElements colToArray(toCheck - n)) == half - idx) half
              else findAcc(idx, toCheck - 1)
            }
            else findAcc(idx, toCheck - 1)
          }
          else findAcc(idx, toCheck - 1)
        }
        else findAcc(idx + 1, rows - 1)
      }
    }

    findAcc(0, rows - 1)
  }

  private def checkMirrors(elements: List[Array[Array[Char]]]): Unit = {
    val result = elements.map(e => (findHorizontal2(e), findVertical2(e)))

    println(result.filter(_ == (-1,-1)))
    println(result.filter(e => e._1 != -1 && e._2 != -1))
  }

//  println(calculateResult(patternsE))
//  println(calculateResult(patterns)) //27249 too low , 27389 wrong

  checkMirrors(patterns)

}
