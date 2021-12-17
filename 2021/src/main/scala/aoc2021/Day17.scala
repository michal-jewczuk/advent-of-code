package aoc2021

import scala.annotation.tailrec

object Day17 extends App {

  val targetE = ((20,30), (-10,-5))
  val target = ((150,193), (-136,-86))

  def findStartingRanges(target: ((Int, Int), (Int, Int))): ((Int, Int), (Int, Int)) = {
    @tailrec
    def sumUpTo(to: Int, current: Int): Int = {
      if (2 * to == current * (current + 1) || 2 * to < current * (current + 1)) current
      else sumUpTo(to, current + 1)
    }

    val minY = target._2._1
    val maxY = -1 * target._2._1 - 1
    val minX = sumUpTo(target._1._1, 1)
    val maxX = target._1._2

    ((minX,maxX), (minY,maxY))
  }

  def findAllInTarget(target: ((Int, Int), (Int, Int))): Set[(Int, Int)] = {
    val initial = findStartingRanges(target)

    @tailrec
    def willLandInTarget(x: Int, y: Int, accX: Int, accY: Int): Boolean = {
      if (accX > target._1._2 || accY < target._2._1) false
      else if (accX >= target._1._1 && accX <= target._1._2 && accY >= target._2._1 && accY <= target._2._2) true
      else willLandInTarget(if x == 0 then 0 else x - 1, y - 1, accX + x, accY + y)
    }

    (for {
      x <- initial._1._1 to initial._1._2
      y <- initial._2._1 to initial._2._2
    } yield (x,y)).filter(p => willLandInTarget(p._1, p._2, 0, 0)).toSet
  }

  println(findAllInTarget(targetE).map(p => p._2 * (p._2 + 1) / 2).max)
  println(findAllInTarget(target).map(p => p._2 * (p._2 + 1) / 2).max)
  println("----------")
  println(findAllInTarget(targetE).size)
  println(findAllInTarget(target).size)
}
