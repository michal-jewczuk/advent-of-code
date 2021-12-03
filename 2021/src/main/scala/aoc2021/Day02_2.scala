package aoc2021

import scala.annotation.tailrec

object Day02_2 extends App {

  val directionsExample = loadData("d02e.txt").get
    .map(line => line.split(" "))
    .map(a => (a(0), a(1).toInt))
  val directions = loadData("d02.txt").get
    .map(line => line.split(" "))
    .map(a => (a(0), a(1).toInt))

  def calculatePosition(directions: List[(String, Int)]): (Int, Int) = {
    @tailrec
    def accumulate(all: List[(String, Int)], accumulators: (Int, Int, Int)): (Int, Int) = {
      if (all.isEmpty) (accumulators._1, accumulators._2)
      else accumulate(all.tail, adjustForwardDepthAim(all.head, accumulators))
    }

    def adjustForwardDepthAim(command: (String, Int), metrics: (Int, Int, Int)): (Int, Int, Int) = {
      if (command._1.equals("down")) (metrics._1, metrics._2, metrics._3 + command._2)
      else if (command._1.equals("up")) (metrics._1, metrics._2, metrics._3 - command._2)
      else (metrics._1 + command._2, metrics._2 + command._2 * metrics._3, metrics._3)
    }

    accumulate(directions, (0, 0, 0))
  }

  println(calculatePosition(directionsExample))

  val position = calculatePosition(directions)
  println(position._1 * position._2)
}
