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
      val (forward, depth, aim) = metrics
      val (direction, value) = command
      if (direction.equals("down")) (forward, depth, aim + value)
      else if (direction.equals("up")) (forward, depth, aim - value)
      else (forward + value, depth + value * aim, aim)
    }

    accumulate(directions, (0, 0, 0))
  }

  println(calculatePosition(directionsExample))

  val position = calculatePosition(directions)
  println(position._1 * position._2)
}
