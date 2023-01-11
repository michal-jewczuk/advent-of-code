package aoc2022

import scala.annotation.tailrec

object Day18 extends App {

  val linesE = loadStrings("d18e.txt")
  val lines = loadStrings("d18.txt")

  def transformData(input: List[String]): List[(Int,Int,Int)] = {
    input.map(_.split(",")).map(a => (a(0).toString.toInt, a(1).toString.toInt, a(2).toString.toInt))
  }

  def generateAdjescent(cube: (Int,Int,Int)): List[(Int,Int,Int)] = {
    List((cube._1 - 1, cube._2, cube._3), (cube._1 + 1, cube._2, cube._3),
      (cube._1, cube._2 - 1, cube._3), (cube._1, cube._2 + 1, cube._3),
      (cube._1, cube._2, cube._3 - 1), (cube._1, cube._2, cube._3 + 1))
  }

  def countSurface(input: List[String]): Int = {
    val allCubes = transformData(input)
    @tailrec
    def countUntil(cubes: List[(Int,Int,Int)], accCount: Int): Int = {
      if (cubes.isEmpty) accCount
      else countUntil(cubes.tail, accCount + 6 - generateAdjescent(cubes.head).count(allCubes.contains(_)))
    }

    countUntil(allCubes, 0)
  }

  def countSurfaceWithoutAir(input: List[String]): Int = {
    val allCubes = transformData(input)
    val low = (allCubes.map(_._1).min, allCubes.map(_._2).min, allCubes.map(_._3).min)
    val high = (allCubes.map(_._1).max, allCubes.map(_._2).max, allCubes.map(_._3).max)

    val allToConsider = (low._1 to high._1).flatMap(x => (low._2 to high._2).flatMap(y => (low._3 to high._3).map(z => (x,y,z))))
        .toSet.filter(!allCubes.contains(_))
    val onSides = allToConsider.filter(_._1 == low._1) ++ allToConsider.filter(_._2 == low._2) ++ allToConsider.filter(_._3 == low._3) ++
      allToConsider.filter(_._1 == high._1) ++ allToConsider.filter(_._2 == high._2) ++ allToConsider.filter(_._3 == high._3)

    @tailrec
    def findAirUntil(toProcess: Set[(Int,Int,Int)], nonAcc: Set[(Int,Int,Int)]): Set[(Int,Int,Int)] = {
      if (toProcess.isEmpty) nonAcc
      else {
        val accessible = generateAdjescent(toProcess.head).filter(nonAcc.contains(_))
        findAirUntil(toProcess.tail ++ accessible, nonAcc -- accessible)
      }
    }

    @tailrec
    def countAirUntil(cubes: Set[(Int,Int,Int)], accCount: Int): Int = {
      if (cubes.isEmpty) accCount
      else countAirUntil(cubes.tail, accCount + generateAdjescent(cubes.head).count(allCubes.contains(_)))
    }

    countSurface(input) - countAirUntil(findAirUntil(onSides, allToConsider -- onSides),0)
  }

  println(countSurface(linesE))
  println(countSurface(lines))
  println("================")
  println(countSurfaceWithoutAir(linesE))
  println(countSurfaceWithoutAir(lines))
}
