package aoc2024

import scala.annotation.tailrec

object Day02 extends App {

  private val linesE = loadData("d02e.txt").get
  private val lines = loadData("d02.txt").get

  private def extractData(lines: List[String]): List[List[Int]] = {
    lines.map(_.split(" ").toList).map(_.map(_.toInt))
  }

  private def calculateDiffs(report: List[Int]): List[Int] = {
    @tailrec def calculateUntil(report: List[Int], acc: List[Int]): List[Int] = {
      if (report.tail.isEmpty) acc
      else {
        val el = report.head - report.tail.head
        calculateUntil(report.tail, acc :+ el)
      }
    }

    calculateUntil(report, List.empty)
  }

  private def isReportSafe(report: List[Int]): Boolean = {
    val diffs = calculateDiffs(report)
    val first = diffs.head
    if (first == 0) return false

    val order = if (first > 0) diffs.count(_ > 0) == diffs.length else diffs.count(_ < 0) == diffs.length
    val diffSize = diffs.count(a => (Math.abs(a) > 0 && Math.abs(a) < 4)) == diffs.length

    order && diffSize
  }

  private def findSafeReports(reports: List[List[Int]]): Int = {
    reports.count(isReportSafe)
  }

  private def isReportSafeWithDampener(report: List[Int]): Boolean = {
    if (isReportSafe(report)) return true
    if (isReportSafe(report.tail)) return true

    @tailrec def calculateUntil(acc: Int): Boolean = {
      if (acc > report.length) false
      else {
        if (isReportSafe(report.slice(0, acc) ++ report.slice(acc + 1, report.length))) true
        else calculateUntil(acc+1)
      }
    }

    calculateUntil(1)
  }

  private def findSafeReportsWithDampener(reports: List[List[Int]]): Int = {
    reports.count(isReportSafeWithDampener)
  }

  println(findSafeReports(extractData(linesE)))
  println(findSafeReports(extractData(lines)))
  println("===============")
  println(findSafeReportsWithDampener(extractData(linesE)))
  println(findSafeReportsWithDampener(extractData(lines)))
}
