package aoc2021

import scala.annotation.tailrec
import scala.util.Try

object Day11_1 extends App {

  val octopusesE = convertToArray(loadData("d11e.txt").get)
  val octopuses = convertToArray(loadData("d11.txt").get)

  def simulateOneStep(octs: Array[Array[Int]]) = {
    var internal: Array[Array[Int]] = octs

    def increaseValues = {
      octs.indices.map(row => octs(row).indices.map(col => octs(row)(col) + 1).toArray).toArray
    }

    def resetFlashed = {
      internal.indices.map(row =>
        internal(row).indices.map(col =>
          if (internal(row)(col) > 9) 0
          else internal(row)(col)).toArray)
        .toArray
    }
    
    def flash(row: Int, col: Int): Unit = {
      Seq((1,0), (1,-1), (0,-1), (-1,-1), (-1,0), (-1,1), (0,1), (1,1))
        .filter(point => Try(internal(row + point._1)(col + point._2)).isSuccess)
        .foreach(point => internal(point._1 + row)(point._2 + col) = internal(point._1 + row)(point._2 + col) + 1)
    }

    def findAllToFlash(checked: List[(Int, Int)]): List[(Int, Int)] = {
      internal.indices.flatMap(row => internal.indices.map(col =>
        if (internal(row)(col) > 9 && !checked.contains((row, col))) (row, col)
        else (-1,-1)))
        .filter(_ != (-1, -1)).toList
    }

    @tailrec
    def flashAll(flashed: List[(Int, Int)]): List[(Int, Int)] = {
      val newOnes = findAllToFlash(flashed)
      newOnes.foreach(p => flash(p._1, p._2))
      if (newOnes.isEmpty) flashed
      else flashAll(flashed ++ newOnes)
    }

    internal = increaseValues
    val flashed = flashAll(List())
    (resetFlashed, flashed)
  }

  def simulateNTimes(octs: Array[Array[Int]], times: Int) = {
    var internal = octs
    var accumulator = 0
    (1 to times).foreach(n => {
      val (int, flashed) = simulateOneStep(internal)
      internal = int
      accumulator += flashed.size
    })
    accumulator
  }

  def findFirstGroupFlash(octs: Array[Array[Int]]): Int = {
    var internal = octs
    (0 to 1000).foreach(n => {
      val (int, flashed) = simulateOneStep(internal)
      if (flashed.size == 100) return n + 1
      else internal = int
    })
    -1
  }

  println(simulateNTimes(octopusesE,100))
  println(simulateNTimes(octopuses,100))

  println(findFirstGroupFlash(octopusesE))
  println(findFirstGroupFlash(octopuses))
}
