package aoc2022

import scala.annotation.tailrec

object Day09 extends App {

  val linesE1 = loadStrings("d09e.txt")
  val linesE2 = loadStrings("d09e2.txt")
  val lines = loadStrings("d09.txt")
  val offsets: Map[String, (Int, Int)] = Map("R" -> (1,0), "L" -> (-1,0), "U" -> (0,1), "D" -> (0,-1))

  private def extractMoves(input: List[String]): List[((Int,Int),Int)] = {
    input.map(l => l.split(" ")).map(e => (offsets(e(0).toString), e(1).toInt))
  }

  private def performSingleMove(hPos: (Int, Int), tPos: (Int, Int), offset: (Int, Int)): ((Int,Int), (Int,Int)) = {
    val hP = (hPos._1 + offset._1, hPos._2 + offset._2)
    if ((hP._1 - tPos._1).abs <= 1 && (hP._2 - tPos._2).abs <= 1) (hP, tPos)
    else if ((hP._1 - tPos._1).abs <= 1 && (hP._2 - tPos._2).abs == 2) (hP, (hP._1, tPos._2 + (hP._2 - tPos._2)/2))
    else if ((hP._2 - tPos._2).abs <= 1 && (hP._1 - tPos._1).abs == 2) (hP, (tPos._1 + (hP._1 - tPos._1)/2, hP._2))
    else (hP, (tPos._1 + (hP._1 - tPos._1)/(2), tPos._2 + (hP._2 - tPos._2)/(2)))
  }

  private def simulateFirstTail(input: List[String]): Set[(Int,Int)] = {
    @tailrec
    def processUntil(left: List[((Int,Int),Int)], hPos: (Int, Int), tPos: (Int,Int), visited: Set[(Int, Int)]): Set[(Int,Int)] = {
      if (left.isEmpty) visited
      else {
        val (hP, tP, tVisited) = moveInOneDirection(left.head, hPos, tPos, Set.empty, 0)
        processUntil(left.tail, hP, tP, visited ++ tVisited)
      }
    }

    @tailrec
    def moveInOneDirection(line: ((Int,Int),Int), hPos: (Int, Int), tPos: (Int,Int), visited: Set[(Int, Int)], counter: Int): ((Int,Int), (Int,Int), Set[(Int,Int)]) = {
      if (counter >= line._2) (hPos, tPos, visited)
      else {
        val (hP, tP) = performSingleMove(hPos, tPos, line._1)
        moveInOneDirection(line, hP, tP, visited + tP, counter + 1)
      }
    }

    processUntil(extractMoves(input), (0,0), (0,0), Set((0,0)))
  }

  private def simulateNinthTail(input: List[String]): Set[(Int,Int)] = {
    @tailrec
    def processUntil(left: List[((Int,Int),Int)], knots: List[(Int,Int)], visited: Set[(Int, Int)]): Set[(Int,Int)] = {
      if (left.isEmpty) visited
      else {
        val (nKnots, nVisited) = moveInOneDirection(left.head, knots, Set.empty, 0)
        processUntil(left.tail, nKnots, visited ++ nVisited)
      }
    }

    @tailrec
    def moveInOneDirection(line: ((Int,Int),Int), knots: List[(Int,Int)], visited: Set[(Int, Int)], counter: Int): (List[(Int,Int)], Set[(Int,Int)]) = {
      if (counter >= line._2) (knots, visited)
      else {
        val nKnots = moveAllKnots(knots, line._1)
        moveInOneDirection(line, nKnots, visited + nKnots.last, counter + 1)
      }
    }

    def moveAllKnots(knots: List[(Int,Int)], offset: (Int,Int)): List[(Int,Int)] = {
      @tailrec
      def moveUntil(tails: List[(Int, Int)], moved: List[(Int, Int)]): List[(Int,Int)] = {
        if (tails.tail.isEmpty) moved
        else if (moved.last == tails.head) moved ++ tails.tail
        else {
          val (p1, p2) = performSingleMove(tails.head, tails.tail.head, (moved.last._1 - tails.head._1, moved.last._2 - tails.head._2))
          moveUntil(tails.tail, moved :+ p2)
        }
      }

      val (hP, t1P) = performSingleMove(knots.head, knots.tail.head, offset)
      moveUntil(knots.tail, List(hP, t1P))
    }

    processUntil(extractMoves(input), (0 to 9).map(i => (0,0)).toList, Set((0,0)))
  }

  println(simulateFirstTail(linesE1).size)
  println(simulateFirstTail(lines).size)
  println("================")
  println(simulateNinthTail(linesE1).size)
  println(simulateNinthTail(linesE2).size)
  println(simulateNinthTail(lines).size)
}
