package aoc2023

import scala.annotation.tailrec

object Day06 extends App {

  private val timesE = List((7,9), (15,40), (30,200))
  private val times = List((40,215), (70,1051), (98,2147), (79,1005))
  private val combinedE = (71530, 940200)
  private val combined = (40709879, 215105121471005L)

  private def calculateWays(time: Long, dist: Long): Long = {
    @tailrec def calculateAcc(acc: Long): Long = {
      if (acc > time / 2) 0 // sanity check
      else if (acc * (time - acc) > dist) (time + 1) - acc * 2
      else calculateAcc(acc + 1)
    }

    calculateAcc(0)
  }

  println(timesE.map(t => calculateWays(t._1, t._2)).product)
  println(times.map(t => calculateWays(t._1, t._2)).product)
  println("=======================")
  println(calculateWays(combinedE._1, combinedE._2))
  println(calculateWays(combined._1, combined._2))
}
