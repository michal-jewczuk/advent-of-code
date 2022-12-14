package aoc2022

import scala.annotation.tailrec

object Day14 extends App {

  val linesE = loadStrings("d14e.txt")
  val lines = loadStrings("d14.txt")

  private def processPaths(input: List[String]): Set[(Int,Int)] = {
    @tailrec
    def processUntil(l: List[String], acc: Set[(Int,Int)]): Set[(Int,Int)] = {
      if (l.isEmpty) acc
      else {
        val newE = l.head.split(" -> ").map(e => e.split(',')).map(v => (v(0).toInt, v(1).toInt)).toList
        processUntil(l.tail, acc ++ fillSpace(newE, List.empty))
      }
    }

    @tailrec
    def fillSpace(points: List[(Int,Int)], acc: List[(Int,Int)]): List[(Int,Int)] = {
      if (points.tail.isEmpty) acc
      else fillSpace(points.tail, acc ++ fillSingleLine(points.head, points.tail.head))
    }

    def fillSingleLine(start: (Int,Int), end: (Int, Int)): List[(Int,Int)] = {
      val step = if (start._1 == end._1) (end._2 - start._2) / (end._2 - start._2).abs else (end._1 - start._1) / (end._1 - start._1).abs

      if (start._1 == end._1) (start._2 to end._2 by step).map(i => (start._1, i)).toList
      else (start._1 to end._1 by step).map(i => (i, start._2)).toList
    }

    processUntil(input, Set.empty)
  }

  private def simulateBoth(input: List[String], toVoid: Boolean): Int = {
    val sandStart = (500,0)
    val start = processPaths(input)
    val voidLevel = start.map(_._2).max + 1

    @tailrec
    def simulateUntil(state: Set[(Int,Int)], count: Int, last: (Int,Int)): Int = {
      val check = if (toVoid) (-1,-1) else sandStart
      val countA = if (toVoid) count - 1 else count
      if (last == check) countA
      else {
        val stateN = state + last
        simulateUntil(stateN, count + 1, simulateOne(state ++ stateN, voidLevel, sandStart))
      }
    }

    @tailrec
    def simulateOne(state: Set[(Int,Int)], void: Int, at: (Int,Int)): (Int,Int) = {
      val toReturn = if (toVoid) (-1,-1) else at
      if (at._2 >= void) toReturn
      else {
        val down = (at._1, at._2 + 1)
        val left = (at._1 - 1, at._2 + 1)
        val right = (at._1 + 1, at._2 + 1)
        if (state.contains(down) && state.contains(left) && state.contains(right)) at
        else if (!state.contains(down)) simulateOne(state, void, down)
        else if (!state.contains(left)) simulateOne(state, void, left)
        else simulateOne(state, void, right)
      }
    }

    simulateUntil(start, 0, start.toList.head)
  }

  println(simulateBoth(linesE,true))
  println(simulateBoth(lines,true))
  println("================")
  println(simulateBoth(linesE,false))
  println(simulateBoth(lines,false))
}
