package aoc2023

import scala.annotation.tailrec

object Day09 extends App {

  private val linesE = loadData("d09e.txt").get
  private val lines = loadData("d09.txt").get

  private def extractData(all: List[String]): List[List[Long]] = {
    all.map(l => l.split(" ").toList.map(_.toLong))
  }

  private def processOneList(values: List[Long], first: Boolean): List[Long] = {

    @tailrec def processAcc(currStep: List[Long], acc: List[Long]): List[Long] = {
      if (currStep.count(_ == 0L) == currStep.size) acc
      else if (currStep.count(_ > 100L) == currStep.size) acc
      else {
        val diffs = calcDiff(currStep.head, currStep.tail, List.empty)
        if (first) processAcc(diffs, acc :+ diffs.head)
        else processAcc(diffs, acc :+ diffs.reverse.head)
      }
    }

    @tailrec def calcDiff(curr: Long, toProcess: List[Long], acc: List[Long]): List[Long] = {
      if (toProcess.isEmpty) acc
      else calcDiff(toProcess.head, toProcess.tail, acc :+ (toProcess.head - curr))
    }

    processAcc(values, List.empty)
  }

  private def extrapolateListsLast(all: List[List[Long]]): List[Long] = {
    all.map(l => processOneList(l, false).sum + l.reverse.head)
  }

  private def extrapolateListsFirst(all: List[List[Long]]): List[Long] = {
    val res = all.map(l => l.head +: processOneList(l, true))
    @tailrec def calcFirstAcc(toProcess: List[Long], acc: Long): Long = {
      if (toProcess.isEmpty) acc
      else calcFirstAcc(toProcess.tail, toProcess.head - acc)
    }

    res.map(l => calcFirstAcc(l.reverse, 0L))
  }

  println(extrapolateListsLast(extractData(linesE)).sum)
  println(extrapolateListsLast(extractData(lines)).sum) // 1743490457
  println("================")
  println(extrapolateListsFirst(extractData(linesE)).sum)
  println(extrapolateListsFirst(extractData(lines)).sum) // 1053
}
