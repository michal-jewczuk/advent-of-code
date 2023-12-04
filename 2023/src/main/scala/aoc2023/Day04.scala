package aoc2023

import scala.annotation.tailrec

object Day04 extends App {

  private val linesE = loadData("d04e.txt").get
  private val lines = loadData("d04.txt").get

  private def processLines(lines: List[String]): Int = {
    lines.map(processSingleLine).map(line => line._2.count(line._3.contains(_)))
      .map(c => scala.math.pow(2, c - 1)).map(_.toInt).sum
  }

  private def processSingleLine(line: String): (Int, List[Int], List[Int]) = {
    val parts = line.split('|')
    val have = parts(1).trim.split(" ").filter(_.nonEmpty).map(_.toInt).toList
    val winning = parts(0).trim.split(':')(1).trim.split(" ").filter(_.nonEmpty).map(_.toInt).toList
    val cardN = parts(0).trim.split(':')(0).substring(5).trim.toInt

    (cardN, winning, have)
  }

  private def countWinningCards(lines: List[String]): Int = {
    val result = lines.map(processSingleLine).map(line => (line._1, line._2.count(line._3.contains(_))))

    @tailrec def countAcc(toProcess: List[(Int, Int)], acc: Map[Int, Int]): Int = {
      if (toProcess.isEmpty) acc.values.sum
      else {
        val tmp = (0 until toProcess.head._2)
          .map(_ + toProcess.head._1 + 1)
          .map(k => (k, acc(toProcess.head._1) + acc(k))).toMap
        countAcc(toProcess.tail, acc ++ tmp)
      }
    }

    countAcc(result, result.map(c => (c._1, 1)).toMap)
  }

  println(processLines(linesE))
  println(processLines(lines)) //21568
  println("==============")
  println(countWinningCards(linesE))
  println(countWinningCards(lines)) //11827296
}
