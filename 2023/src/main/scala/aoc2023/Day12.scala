package aoc2023

import scala.annotation.tailrec

object Day12 extends App {

  private val linesE = loadData("d12e.txt").get
  private val lines = loadData("d12.txt").get

  private def extractData(lines: List[String]): List[(String, List[Int])] = {
    lines.map(_.split(" ")).map(l => (l(0), l(1).split(",").map(_.toInt).toList))
  }

  private def fuzzyMatches(line: String, pattern: List[Int]): Boolean = {
    @tailrec def matchesAcc(idx: Int, count: Int, groups: List[Int]): Boolean = {
      if (idx == line.length) {
        if (groups.isEmpty) true
        else if (groups.size == 1 && groups.head == count) true
        else false
      }
      else {
        if (line(idx) == '?') true
        else if (line(idx) == '.') {
          if (count == 0) matchesAcc(idx + 1, count, groups)
          else if (count != groups.head) false
          else matchesAcc(idx + 1, 0, groups.tail)
        } else {
          if (groups.isEmpty || count + 1 > groups.head) false
          else matchesAcc(idx + 1, count + 1, groups)
        }
      }
    }

    matchesAcc(0, 0, pattern)
  }

  private def createMatching(initial: String, pattern: List[Int]): List[String] = {
    val positions = initial.toCharArray.indices.map(n => if (initial(n) == '?') n else - 1).filter(_ != -1).toList

    @tailrec def createMatchingAcc(pos: List[Int], acc: List[String]): List[String] = {
      if (pos.isEmpty) acc
      else {
        val possibleMatches = acc.flatMap(extractValid(pos.head, _))
        createMatchingAcc(pos.tail, possibleMatches)
      }
    }

    def extractValid(pos: Int, current: String): List[String] = {
      List('.', '#')
        .map(c => current.substring(0, pos) + c + current.substring(pos + 1))
        .filter(fuzzyMatches(_, pattern))
    }

    createMatchingAcc(positions, List(initial))
  }

  private def calculateSum(lines: List[String]): Int = {
    extractData(lines).map(l => createMatching(l._1, l._2).size).sum
  }

  // too optimistic
  private def calculateSumFolded(lines: List[String]): Long = {
    extractData(lines)
      .map(l => (l._1 + '?' + l._1 + '?' + l._1 + '?' + l._1 + '?' + l._1, l._2 ++ l._2 ++ l._2 ++ l._2 ++ l._2))
      .map(l => createMatching(l._1, l._2).size).sum
  }

  println(calculateSum(linesE))
  println(calculateSum(lines)) //6488
  println("==================")
//  println(calculateSumFolded(linesE))
//  println(calculateSumFolded(lines))
}
