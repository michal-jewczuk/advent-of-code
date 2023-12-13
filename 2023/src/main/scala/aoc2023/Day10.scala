package aoc2023

import scala.annotation.tailrec
import scala.util.Try

object Day10 extends App {

  private val loopE1 = loadStrings("d10e1.txt").map(_.toArray).toArray
  private val loopE2 = loadStrings("d10e2.txt").map(_.toArray).toArray
  private val loop = loadStrings("d10.txt").map(_.toArray).toArray

  private val connects: Map[Char, List[(Int, Int)]] = Map(
    '|' -> List((1,0), (-1,0)),
    '-' -> List((0,-1), (0,1)),
    'L' -> List((-1,0), (0,1)),
    'J' -> List((-1,0), (0,-1)),
    '7' -> List((1,0), (0,-1)),
    'F' -> List((1,0), (0,1)),
    '.' -> List((0,0))
  )

  /*
  | is a vertical pipe connecting north and south.
  - is a horizontal pipe connecting east and west.
  L is a 90-degree bend connecting north and east.
  J is a 90-degree bend connecting north and west.
  7 is a 90-degree bend connecting south and west.
  F is a 90-degree bend connecting south and east.
  . is ground; there is no pipe in this tile.
   */

  private def findStart(ground: Array[Array[Char]]): (Int, Int) = {
    ground.indices.flatMap(r => ground(r).indices.map(c => if (ground(r)(c) != 'S') (-1,-1) else (r,c)).filter(_ != (-1,-1)))(0)
  }

  private def findPossibleConn(ground: Array[Array[Char]]): List[(Int,Int)] = {
    val start = findStart(ground)
    List((1,0), (-1,0), (0,-1), (0,1))
      .map(e => (start._1 + e._1, start._2 + e._2))
      .filter(isAbleToConnect(_, start, ground))
  }

  private def isAbleToConnect(p1: (Int, Int), p2: (Int, Int), ground: Array[Array[Char]]): Boolean = {
    val p1Char = Try(ground(p1._1)(p1._2))
    if (p1Char.isFailure) false
    else connects(p1Char.get).map(c => (c._1 + p1._1, c._2 + p1._2)).count(_ == p2) == 1
  }

  private def findMaxDistance(ground: Array[Array[Char]]): Int = {
    val start = findStart(ground)
    val firstSteps = findPossibleConn(ground)

    @tailrec def traverse(cur1: (Int, Int), prev1: (Int, Int), cur2: (Int, Int), prev2: (Int, Int), acc: Int): Int = {
      if (cur1 == cur2) acc
      else {
        val next1 = connects(ground(cur1._1)(cur1._2)).map(c => (c._1 + cur1._1, c._2 + cur1._2)).filter(_ != prev1).head
        val next2 = connects(ground(cur2._1)(cur2._2)).map(c => (c._1 + cur2._1, c._2 + cur2._2)).filter(_ != prev2).head
        traverse(next1, cur1, next2, cur2, acc + 1)
      }
    }

    traverse(firstSteps.head, start, firstSteps.tail.head, start, 1)
  }

  println(findMaxDistance(loopE1))
  println(findMaxDistance(loopE2))
  println(findMaxDistance(loop)) //6823
  println("============")
}
